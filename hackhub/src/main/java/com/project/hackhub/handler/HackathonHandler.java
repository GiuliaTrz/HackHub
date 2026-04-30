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

        if(dto.judge() != null || !dto.mentorsList().isEmpty())
            throw new IllegalArgumentException("Staff roles cannot be modified through this endpoint;" +
                    "Please use the appropriate staff management endpoints.");
      
        hackathon.setName(dto.name());
        hackathon.setRuleBook(dto.ruleBook());
        hackathon.setExpiredSubscriptionsDate(dto.expiredSubscriptionsDate());
        hackathon.setMaxTeamDimension(dto.maxTeamDimension());
        hackathon.setMoneyPrice(dto.moneyPrice());

        return hackathonRepo.save(hackathon);
    }
}