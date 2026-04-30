package com.project.hackhub.boundary;

import com.project.hackhub.dto.InfractionDTO;
import com.project.hackhub.handler.InfractionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller per la gestione delle infrazioni all'interno degli hackathon.
 * Espone endpoint per segnalare, gestire, penalizzare ed eliminare infrazioni.
 */
@RestController
@RequestMapping("api/infraction")
@RequiredArgsConstructor
public class InfractionBoundary {

    private final InfractionHandler infractionHandler;

    /**
     * Segnala una nuova infrazione per un team.
     *
     * @param mentor ID del mentor autenticato che segnala l'infrazione
     * @param dto DTO contenente i dettagli dell'infrazione
     * @return ResponseEntity vuoto con status 200 OK
     */
    @PostMapping("/report")
    public ResponseEntity<String> reportInfraction(
            @AuthenticationPrincipal UUID mentor,
            @RequestBody InfractionDTO dto) {

        infractionHandler.reportInfraction(mentor, dto);
        return ResponseEntity.ok("infraction has been successfully reported");
    }

    /**
     * Espelle un team dall'hackathon.
     *
     * @param coordinator ID del coordinatore autenticato
     * @param team ID del team da espellere
     * @return messaggio di conferma dell'operazione
     */
    @DeleteMapping("/{team}/expel")
    public ResponseEntity<String> expelTeam(
            @AuthenticationPrincipal UUID coordinator,
            @PathVariable UUID team) {

        infractionHandler.expelTeam(team, coordinator);
        return ResponseEntity.ok("team has been successfully expelled");
    }

    /**
     * Penalizza un team sottraendo un certo numero di punti.
     *
     * @param coordinator ID del coordinatore autenticato
     * @param team ID del team da penalizzare
     * @param pointsToDeduct numero di punti da sottrarre
     * @return messaggio di conferma dell'operazione
     */
    @PatchMapping("/{team}/penalize")
    public ResponseEntity<String> penalizeTeam(
            @AuthenticationPrincipal UUID coordinator,
            @PathVariable UUID team,
            @RequestBody float pointsToDeduct) {

        infractionHandler.penalizeTeam(coordinator, team, pointsToDeduct);
        return ResponseEntity.ok("team will be penalized subtracting " + pointsToDeduct + " points from final grade");
    }

    /**
     * Gestisce la presenza di un'infrazione per un team.
     * Il coordinatore potrà successivamente decidere se penalizzare o espellere il team.
     *
     * @param coordinator ID del coordinatore autenticato
     * @param team ID del team con infrazione
     * @return messaggio informativo
     */
    @PostMapping("/handle")
    public ResponseEntity<String> handleInfraction(
            @AuthenticationPrincipal UUID coordinator,
            @RequestBody UUID team) {

        infractionHandler.handleInfraction(coordinator, team);
        return ResponseEntity.ok("Infraction to be handled: please, penalize or expel team");
    }

    /**
     * Elimina una specifica infrazione da un hackathon.
     *
     * @param userId ID dell'utente autenticato
     * @param hackathonId ID dell'hackathon
     * @param infractionIndex indice dell'infrazione da rimuovere
     * @return ResponseEntity vuoto con status 204 No Content
     */
    @DeleteMapping("/{hackathonId}/{infractionIndex}")
    public ResponseEntity<Void> deleteInfraction(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID hackathonId,
            @PathVariable int infractionIndex) {

        infractionHandler.deleteInfraction(userId, hackathonId, infractionIndex);
        return ResponseEntity.noContent().build();
    }
}