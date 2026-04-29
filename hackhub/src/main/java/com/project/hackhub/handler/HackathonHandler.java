package com.project.hackhub.handler;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.observer.EliminazioneHackathonListener;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import com.project.hackhub.service.UserStateService;
import jakarta.transaction.Transactional;
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
    private final UtenteRegistratoRepository utenteRepository;
    private final UserStateService userStateService;
    private final EliminazioneHackathonListener eliminazioneHackathonListener;

    /**
     * Elimina un hackathon (solo organizzatore con permessi globali).
     * Prima di cancellare, raccoglie tutti i partecipanti (staff + membri team)
     * e li passa al listener per il reset degli stati.
     */
    @Transactional
    public void deleteHackathon(UUID deleterId, UUID hackathonId) {

        UtenteRegistrato deleter = utenteRepository.findById(deleterId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!deleter.hasPermission(Permission.CAN_DELETE_HACKATHON, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to delete hackathon");
        }

        // 1. Raccolta di tutti gli utenti coinvolti
        List<UtenteRegistrato> partecipanti = new ArrayList<>();

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
        List<UtenteRegistrato> utentiUnici = partecipanti.stream()
                .distinct()
                .collect(Collectors.toList());

        // 2. Notifica al listener per il reset degli stati
        eliminazioneHackathonListener.updateUsers(utentiUnici, "Hackathon eliminato", hackathon);

        // 3. Eliminazione dell'hackathon
        hackathonRepo.delete(hackathon);
    }

    /**
     * Modifica i dati di base di un hackathon usando un DTO.
     * La prenotazione non viene mai modificata.
     */
    @Transactional
    public Hackathon updateHackathon(UUID editorId, UUID hackathonId, HackathonDTO dto) {
        UtenteRegistrato editor = utenteRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!editor.hasPermission(Permission.CAN_MODIFY_HACKATHON, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions");
        }

        // Aggiorna solo i campi modificabili; la prenotazione viene ignorata
        hackathon.setName(dto.name());
        hackathon.setRuleBook(dto.ruleBook());
        hackathon.setExpiredSubscriptionsDate(dto.expiredSubscriptionsDate());
        hackathon.setMaxTeamDimension(dto.maxTeamDimension());
        // Staff e premi vanno modificati tramite appositi metodi

        return hackathonRepo.save(hackathon);
    }

    /**
     * Modifica lo staff dell'hackathon.
     * Regole:
     * - L'organizzatore non può essere rimosso, solo aggiunto se assente.
     * - Il giudice può solo essere sostituito, non rimosso.
     * - Non si può rimuovere l'ultimo mentore.
     */
    @Transactional
    public void modifyStaff(UUID editorId, UUID hackathonId, UUID staffMemberId, String role, boolean add) {
        UtenteRegistrato editor = utenteRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        UtenteRegistrato member = utenteRepository.findById(staffMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found"));

        if (!editor.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to manage staff");
        }

        switch (role.toUpperCase()) {
            case "ORGANIZER":
                if (add) {
                    if (hackathon.getCoordinator() != null) {
                        throw new IllegalStateException("Hackathon ha già un organizzatore.");
                    }
                    hackathon.setCoordinator(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.ORGANIZZATORE);
                } else {
                    if (hackathon.getCoordinator() == null || !hackathon.getCoordinator().getId().equals(staffMemberId)) {
                        throw new IllegalArgumentException("L'utente non è l'organizzatore di questo hackathon");
                    }
                    hackathon.setCoordinator(null);
                    userStateService.changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;

            case "MENTOR":
                if (add) {
                    hackathon.addMentor(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.MENTORE);
                } else {
                    // Controllo: non si può rimuovere l'ultimo mentore
                    if (hackathon.getMentorsList() == null || hackathon.getMentorsList().size() <= 1) {
                        throw new IllegalStateException("Non è possibile rimuovere l'ultimo mentore.");
                    }
                    hackathon.removeMentor(member);
                    userStateService.changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;

            case "JUDGE":
                // Il giudice non può essere rimosso, solo sostituito
                if (add) {
                    if (hackathon.getJudge() != null) {
                        UtenteRegistrato oldJudge = hackathon.getJudge();
                        hackathon.setJudge(null);
                        userStateService.changeUserState(oldJudge, false, hackathon, UserStateType.DEFAULT_STATE);
                    }
                    hackathon.setJudge(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.GIUDICE);
                } else {
                    throw new UnsupportedOperationException("Il giudice non può essere rimosso, può solo essere sostituito.");
                }
                break;

            default:
                throw new IllegalArgumentException("Ruolo non valido. Usare ORGANIZER, MENTOR o JUDGE");
        }

        hackathonRepo.save(hackathon);
        utenteRepository.save(member);
    }
}