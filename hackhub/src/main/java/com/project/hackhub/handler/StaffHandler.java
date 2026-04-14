package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.project.hackhub.service.UserStateService.changeUserState;

@Component
@RequiredArgsConstructor
public class StaffHandler {

    private final HackathonRepository hackathonRepository;
    private final UtenteRegistratoRepository utenteRepository;

    /**
     * Aggiunge un utente come mentore all'hackathon.
     * @param organizerId ID dell'organizzatore che esegue l'azione
     * @param hackathonId ID dell'hackathon
     * @param mentorId ID dell'utente da aggiungere come mentore
     * @throws UnsupportedOperationException se l'organizzatore non ha i permessi
     * @throws IllegalArgumentException se uno degli ID non corrisponde a un'entità
     * @author Giulia Trozzi
     */
    public void addMentor(UUID organizerId, UUID hackathonId, UUID mentorId) {
        UtenteRegistrato organizer = utenteRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        UtenteRegistrato mentor = utenteRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));

        if (!organizer.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to manage staff");
        }

        if (!mentor.isAvailable(hackathon.getReservation())) {
            throw new IllegalStateException("Mentor is not available for this hackathon");
        }

        hackathon.addMentor(mentor);
        changeUserState(mentor, true, hackathon, UserStateType.MENTORE);
        hackathonRepository.save(hackathon);
        utenteRepository.save(mentor);
    }

    /**
     * Rimuove un mentore dall'hackathon (assegna lo stato DEFAULT).
     * @param organizerId ID organizzatore
     * @param hackathonId ID hackathon
     * @param mentorId ID mentore da rimuovere
     * @author Giulia Trozzi
     */
    public void removeMentor(UUID organizerId, UUID hackathonId, UUID mentorId) {
        UtenteRegistrato organizer = utenteRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        UtenteRegistrato mentor = utenteRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentore non trovato"));

        if (!organizer.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Permessi insufficienti per gestire lo staff");
        }

        if (!hackathon.getMentorsList().contains(mentor)) {
            throw new IllegalArgumentException("L'utente non è un mentore di questo hackathon");
        }

        hackathon.removeMentor(mentor);
        changeUserState(mentor, false, hackathon, UserStateType.DEFAULT_STATE);
        hackathonRepository.save(hackathon);
        utenteRepository.save(mentor);
    }

    /**
     * Modifica il ruolo di un utente all'interno di un hackathon.
     * Può essere eseguito solo da un organizzatore con permessi di gestione staff.
     * L'organizzatore non può cambiare il proprio ruolo né lasciare l'hackathon senza organizzatori.
     *
     * @param organizerId  ID dell'organizzatore che esegue l'operazione
     * @param hackathonId  ID dell'hackathon
     * @param targetUserId ID dell'utente di cui si vuole modificare il ruolo
     * @param newRole      nuovo ruolo da assegnare ("ORGANIZER", "MENTOR", "JUDGE")
     * @throws IllegalArgumentException      se uno degli ID non corrisponde a un'entità o se il ruolo non è valido
     * @throws UnsupportedOperationException se l'organizzatore non ha i permessi necessari,
     *                                       se tenta di modificare il proprio ruolo,
     *                                       o se l'operazione lascerebbe l'hackathon senza organizzatori
     * @throws IllegalStateException         se l'utente target non è disponibile (ha già uno stato incompatibile)
     */
    public void changeStaffRole(UUID organizerId, UUID hackathonId, UUID targetUserId, String newRole) {
        UtenteRegistrato organizer = utenteRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        UtenteRegistrato targetUser = utenteRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Utente target non trovato"));

        if (!organizer.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Permessi insufficienti per gestire lo staff");
        }

        UserStateType targetState = parseRole(newRole);

        if (organizer.getId().equals(targetUserId)) {
            throw new UnsupportedOperationException("Un organizzatore non può modificare il proprio ruolo");
        }

        boolean isCurrentlyOrganizer = hackathon.getCoordinator() != null &&
                hackathon.getCoordinator().getId().equals(targetUserId);
        if (isCurrentlyOrganizer && targetState != UserStateType.ORGANIZZATORE) {
            throw new UnsupportedOperationException("Impossibile cambiare ruolo all'unico organizzatore dell'hackathon");
        }

        if (!targetUser.isAvailable(hackathon.getReservation())) {
            removeUserFromAllStaffRoles(targetUser, hackathon);
        }

        assignRoleToUser(targetUser, hackathon, targetState);
        changeUserState(targetUser, true, hackathon, targetState);

        hackathonRepository.save(hackathon);
        utenteRepository.save(targetUser);
    }

    private UserStateType parseRole(String role) {
        return switch (role.toUpperCase()) {
            case "ORGANIZER" -> UserStateType.ORGANIZZATORE;
            case "MENTOR" -> UserStateType.MENTORE;
            case "JUDGE" -> UserStateType.GIUDICE;
            default -> throw new IllegalArgumentException("Ruolo non valido. Usare ORGANIZER, MENTOR o JUDGE");
        };
    }

    private void removeUserFromAllStaffRoles(UtenteRegistrato user, Hackathon hackathon) {
        if (hackathon.getMentorsList().contains(user)) {
            hackathon.removeMentor(user);
        }
        if (user.equals(hackathon.getJudge())) {
            hackathon.setJudge(null);
        }
        if (user.equals(hackathon.getCoordinator())) {
            throw new UnsupportedOperationException("Impossibile rimuovere l'organizzatore con questo metodo");
        }
    }

    private void assignRoleToUser(UtenteRegistrato user, Hackathon hackathon, UserStateType role) {
        switch (role) {
            case ORGANIZZATORE -> {
                if (hackathon.getCoordinator() != null) {
                    throw new IllegalStateException("Hackathon ha già un organizzatore. Il modello attuale supporta un solo organizzatore.");
                }
                hackathon.setCoordinator(user);
            }
            case MENTORE -> hackathon.addMentor(user);
            case GIUDICE -> {
                if (hackathon.getJudge() != null) {
                    UtenteRegistrato oldJudge = hackathon.getJudge();
                    hackathon.setJudge(null);
                    changeUserState(oldJudge, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                hackathon.setJudge(user);
            }
            default -> throw new IllegalArgumentException("Ruolo non gestito: " + role);
        }
    }
}