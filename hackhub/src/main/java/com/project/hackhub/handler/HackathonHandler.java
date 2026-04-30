package com.project.hackhub.handler;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.model.user.state.UserStateType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UserRepository;
import com.project.hackhub.service.UserStateService;
import jakarta.transaction.Transactional;
import com.project.hackhub.observer.EliminazioneHackathonListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HackathonHandler {

    private final HackathonRepository hackathonRepo;
    private final UserRepository utenteRepository; // <-- AGGIUNTO
    private final UserStateService userStateService;
    private final EliminazioneHackathonListener eliminazioneHackathonListener;

    /**
     * Deletes a hackathon (only organizer).
     * Before deletion, collects all participants (staff + team members)
     * and passes them to the listener for state reset.
     */
    @Transactional
    public void deleteHackathon(UUID deleterId, UUID hackathonId) {

        User deleter = utenteRepository.findById(deleterId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!deleter.hasPermission(Permission.CAN_DELETE_HACKATHON, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to delete hackathon");
        }

        // 1. Raccolta di tutti gli utenti coinvolti
        List<User> partecipanti = new ArrayList<>();

        // Staff
        if (hackathon.getCoordinator() != null) {
            partecipanti.add(hackathon.getCoordinator());
        }
        if (hackathon.getJudge() != null) {
            partecipanti.add(hackathon.getJudge());
        }
        if (hackathon.getMentorsList() != null) {
            partecipanti.addAll(hackathon.getMentorsList());
        }

        // Membri dei team
        if (hackathon.getTeamsList() != null) {
            for (Team team : hackathon.getTeamsList()) {
                if (team.getTeamMembersList() != null) {
                    partecipanti.addAll(team.getTeamMembersList());
                }
            }
        }

        // Rimozione duplicati
        List<User> utentiUnici = partecipanti.stream()
                .distinct()
                .collect(Collectors.toList());

        // 2. Notify the listener for state reset
        eliminazioneHackathonListener.updateUsers(utentiUnici, "Hackathon deleted", hackathon);

        // 3. Eliminazione dell'hackathon
        hackathonRepo.delete(hackathon);
    }

    /**
     * Modifies the basic data of a hackathon using a DTO.
     * The reservation is never modified.
     */
    @Transactional
    public Hackathon updateHackathon(UUID editorId, UUID hackathonId, HackathonDTO dto) {
        User editor = utenteRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!editor.hasPermission(Permission.CAN_MODIFY_HACKATHON, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions");
        }

        // Updates only modifiable fields; the reservation is ignored
        hackathon.setName(dto.name());
        hackathon.setRuleBook(dto.ruleBook());
        hackathon.setExpiredSubscriptionsDate(dto.expiredSubscriptionsDate());
        hackathon.setMaxTeamDimension(dto.maxTeamDimension());
        // Staff and prizes must be modified through specific methods

        return hackathonRepo.save(hackathon);
    }

    /**
     * Modifies the hackathon staff.
     * Rules:
     * - The organizer cannot be removed, only added if absent.
     * - The judge can only be replaced, not removed.
     * - The last mentor cannot be removed.
     */
    @Transactional
    public void modifyStaff(UUID editorId, UUID hackathonId, UUID staffMemberId, String role, boolean add) {
        User editor = utenteRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        User member = utenteRepository.findById(staffMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found"));

        if (!editor.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to manage staff");
        }

        switch (role.toUpperCase()) {
            case "ORGANIZER":
                if (add) {
                    if (hackathon.getCoordinator() != null) {
                         throw new IllegalStateException("Hackathon already has an organizer.");
                    }
                    hackathon.setCoordinator(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.COORDINATOR);
                } else {
                    if (hackathon.getCoordinator() == null || !hackathon.getCoordinator().getId().equals(staffMemberId)) {
                         throw new IllegalArgumentException("User is not the organizer of this hackathon");
                    }
                    hackathon.setCoordinator(null);
                    userStateService.changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;

            case "MENTOR":
                if (add) {
                    hackathon.addMentor(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.MENTOR);
                } else {
                    // Controllo: non si può rimuovere l'ultimo mentore
                    if (hackathon.getMentorsList() == null || hackathon.getMentorsList().size() <= 1) {
                         throw new IllegalStateException("Cannot remove the last mentor.");
                    }
                    hackathon.removeMentor(member);
                    userStateService.changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;

            case "JUDGE":
                // Il giudice non può essere rimosso, solo sostituito
                if (add) {
                    if (hackathon.getJudge() != null) {
                        User oldJudge = hackathon.getJudge();
                        hackathon.setJudge(null);
                        userStateService.changeUserState(oldJudge, false, hackathon, UserStateType.DEFAULT_STATE);
                    }
                    hackathon.setJudge(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.JUDGE);
                } else {
                    throw new UnsupportedOperationException("Judge cannot be removed, only replaced.");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid role. Use ORGANIZER, MENTOR, or JUDGE");
        }

        hackathonRepo.save(hackathon);
        utenteRepository.save(member);
    }
}