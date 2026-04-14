package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

        // Verifica permesso: solo chi ha permesso CAN_MANAGE_STAFF può aggiungere/rimuovere staff
        if (!organizer.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to manage staff");
        }

        hackathon.addMentor(mentor); // supponiamo esista un metodo addMentor in Hackathon
        hackathonRepository.save(hackathon);
    }

    /**
     * Rimuove un mentore dall'hackathon.
     * @param organizerId ID organizzatore
     * @param hackathonId ID hackathon
     * @param mentorId ID mentore da rimuovere
     * @author Giulia Trozzi
     */
    public void removeMentor(UUID organizerId, UUID hackathonId, UUID mentorId) {
        UtenteRegistrato organizer = utenteRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        UtenteRegistrato mentor = utenteRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));

        if (!organizer.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to manage staff");
        }

        hackathon.removeMentor(mentor);
        hackathonRepository.save(hackathon);
    }
}
