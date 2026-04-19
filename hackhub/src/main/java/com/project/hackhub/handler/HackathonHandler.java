package com.project.hackhub.handler;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.builder.Director;
import com.project.hackhub.model.hackathon.builder.HackathonBuilder;
import com.project.hackhub.model.hackathon.builder.HackathonBuilderMemento;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.HackathonBuilderMementoRepository;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.PrenotazioneRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.project.hackhub.service.UserStateService.changeUserState;

@Component
@RequiredArgsConstructor
public class HackathonHandler {

    private final HackathonRepository hackathonRepo;
    private final UtenteRegistratoRepository utenteRepository; // <-- AGGIUNTO

    /**
     * Elimina un hackathon (solo organizzatore con permessi globali).
     * @param deleterId ID dell'utente che richiede l'eliminazione
     * @param hackathonId ID dell'hackathon
     * @author Giulia Trozzi
     */
    public void deleteHackathon(UUID deleterId, UUID hackathonId) {
        UtenteRegistrato deleter = utenteRepository.findById(deleterId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!deleter.hasPermission(Permission.CAN_DELETE_HACKATHON, null)) {
            throw new UnsupportedOperationException("Insufficient permissions to delete hackathon");
        }

        hackathonRepo.delete(hackathon);
    }

    /**
     * Modifica i dati di un hackathon (nome, descrizione, date, ecc.).
     * @param editorId ID organizzatore
     * @param hackathonId ID hackathon
     * @param updatedHackathon oggetto con i nuovi dati (solo alcuni campi modificabili)
     * @return hackathon aggiornato
     * @author Giulia Trozzi
     */
    public Hackathon updateHackathon(UUID editorId, UUID hackathonId, Hackathon updatedHackathon) {
        UtenteRegistrato editor = utenteRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!editor.hasPermission(Permission.CAN_EDIT_HACKATHON, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions");
        }

        hackathon.setName(updatedHackathon.getName());
        hackathon.setRuleBook(updatedHackathon.getRuleBook());
        hackathon.setExpiredSubscriptionsDate(updatedHackathon.getExpiredSubscriptionsDate());
        hackathon.setMaxTeamDimension(updatedHackathon.getMaxTeamDimension());

        return hackathonRepo.save(hackathon);
    }

    /**
     * Modifica lo staff dell'hackathon
     * Nota: il modello attuale prevede un solo organizzatore
     * @param editorId ID organizzatore
     * @param hackathonId ID hackathon
     * @param staffMemberId ID utente da modificare
     * @param role nuovo ruolo ("ORGANIZER", "MENTOR", "JUDGE")
     * @param add true per aggiungere, false per rimuovere
     * @author Giulia Trozzi
     */
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
                    changeUserState(member, true, hackathon, UserStateType.ORGANIZZATORE);
                } else {
                    if (hackathon.getCoordinator() == null || !hackathon.getCoordinator().getId().equals(staffMemberId)) {
                        throw new IllegalArgumentException("L'utente non è l'organizzatore di questo hackathon");
                    }
                    hackathon.setCoordinator(null);
                    changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;
            case "MENTOR":
                if (add) {
                    hackathon.addMentor(member);
                    changeUserState(member, true, hackathon, UserStateType.MENTORE);
                } else {
                    hackathon.removeMentor(member);
                    changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;
            case "JUDGE":
                if (add) {
                    if (hackathon.getJudge() != null) {
                        UtenteRegistrato oldJudge = hackathon.getJudge();
                        hackathon.setJudge(null);
                        changeUserState(oldJudge, false, hackathon, UserStateType.DEFAULT_STATE);
                    }
                    hackathon.setJudge(member);
                    changeUserState(member, true, hackathon, UserStateType.GIUDICE);
                } else {
                    if (hackathon.getJudge() == null || !hackathon.getJudge().getId().equals(staffMemberId)) {
                        throw new IllegalArgumentException("L'utente non è il giudice di questo hackathon");
                    }
                    hackathon.setJudge(null);
                    changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;
            default:
                throw new IllegalArgumentException("Ruolo non valido. Usare ORGANIZER, MENTOR o JUDGE");
        }
        hackathonRepo.save(hackathon);
        utenteRepository.save(member);
    }
}