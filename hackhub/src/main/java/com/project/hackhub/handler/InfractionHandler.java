package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InfractionHandler {

    private final HackathonRepository hackathonRepository;
    private final UtenteRegistratoRepository utenteRepository;

    /**
     * Elimina una segnalazione di illecito.
     * Poiché Infraction è un @Embeddable senza ID, la identifico tramite hackathonId e indice.
     *
     * @param deleterId       ID dell'utente (organizzatore o mentore)
     * @param hackathonId     ID dell'hackathon
     * @param infractionIndex posizione della segnalazione nella lista
     * @author Giulia Trozzi
     */
    public void deleteInfraction(UUID deleterId, UUID hackathonId, int infractionIndex) {
        UtenteRegistrato deleter = utenteRepository.findById(deleterId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        boolean isOrganizer = deleter.equals(hackathon.getCoordinator());
        boolean isMentor = hackathon.getMentorsList().contains(deleter);
        if (!isOrganizer && !isMentor) {
            throw new UnsupportedOperationException("Solo organizzatore o mentore possono eliminare una segnalazione");
        }

        if (infractionIndex < 0 || infractionIndex >= hackathon.getInfractions().size()) {
            throw new IllegalArgumentException("Indice segnalazione non valido");
        }

        hackathon.getInfractions().remove(infractionIndex);
        hackathonRepository.save(hackathon);
    }
}