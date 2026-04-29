package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.model.user.state.UserStateType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UserRepository;
import com.project.hackhub.service.UserStateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HackathonHandler {

    private final HackathonRepository hackathonRepo;
    private final UserRepository utenteRepository; // <-- AGGIUNTO
    private final UserStateService userStateService;

    /**
     * Elimina un hackathon (solo organizzatore con permessi globali).
     * @param deleterId ID dell'utente che richiede l'eliminazione
     * @param hackathonId ID dell'hackathon
     * @author Giulia Trozzi
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
        hackathonRepo.delete(hackathon);
        //bisogna, prima di eliminarlo, prendere tutti i partecipanti (sia staff che membri dei team)
        //e passarli dentro al listener. Dentro al listener poi cambi lo stato di ogni utente tramite
        // lo userStateService, il metodo è changeUserState, con il false come boolean per rimuovere la prenotazione
    }

    /**
     * Modifica i dati di un hackathon (nome, descrizione, date, ecc.).
     * @param editorId ID organizzatore
     * @param hackathonId ID hackathon
     * @param updatedHackathon oggetto con i nuovi dati (solo alcuni campi modificabili)
     * @return hackathon aggiornato
     * @author Giulia Trozzi
     */
    // bisogna passargli per forza un HackathonDTO [esiste già, lo utilizziamo in fase di creazione anche]
    // non esiste un istanza di hackathon persistita chiamata "updatedHackathon",
    // è un oggetto che contiene i nuovi dati da aggiornare, non è un'entità persistita
    // la prenotazione è immodificabile, anche se è presente nel dto, quindi mettere il controllo
    @Transactional
    public Hackathon updateHackathon(UUID editorId, UUID hackathonId, Hackathon updatedHackathon) {
        User editor = utenteRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!editor.hasPermission(Permission.CAN_MODIFY_HACKATHON, hackathon)) {
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
            // l'organizzatore non si può cambiare, è l'organizzatore stesso che cambia il ruolo di mentori
            // o giudice o aggiunge un nuovo mentore
            case "ORGANIZER":
                if (add) {
                    if (hackathon.getCoordinator() != null) {
                        throw new IllegalStateException("Hackathon ha già un organizzatore.");
                    }
                    hackathon.setCoordinator(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.COORDINATOR);
                } else {
                    if (hackathon.getCoordinator() == null || !hackathon.getCoordinator().getId().equals(staffMemberId)) {
                        throw new IllegalArgumentException("L'utente non è l'organizzatore di questo hackathon");
                    }
                    hackathon.setCoordinator(null);
                    userStateService.changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;
                // non può rimanere senza mentore, bisogna fare il controllo
            // che ne rimanga almeno uno
            case "MENTOR":
                if (add) {
                    hackathon.addMentor(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.MENTOR);
                } else {
                    hackathon.removeMentor(member);
                    userStateService.changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;
            case "JUDGE":
                // non lo può rimuovere, al massimo lo sostituisce, un hackathon non può rimanere senza giudice
                if (add) {
                    if (hackathon.getJudge() != null) {
                        User oldJudge = hackathon.getJudge();
                        hackathon.setJudge(null);
                        userStateService.changeUserState(oldJudge, false, hackathon, UserStateType.DEFAULT_STATE);
                    }
                    hackathon.setJudge(member);
                    userStateService.changeUserState(member, true, hackathon, UserStateType.JUDGE);
                } else {
                    if (hackathon.getJudge() == null || !hackathon.getJudge().getId().equals(staffMemberId)) {
                        throw new IllegalArgumentException("L'utente non è il giudice di questo hackathon");
                    }
                    hackathon.setJudge(null);
                    userStateService.changeUserState(member, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                break;
            default:
                throw new IllegalArgumentException("Ruolo non valido. Usare ORGANIZER, MENTOR o JUDGE");
        }
        hackathonRepo.save(hackathon);
        utenteRepository.save(member);
    }
}