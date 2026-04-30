package com.project.hackhub.boundary;

import com.project.hackhub.handler.ParticipationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller for managing team participation.
 * Handles team subscription and unsubscription operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teamPartecipation")
public class ParticipationBoundary {

    private final ParticipationHandler participationHandler;

    /**
     * Unsubscribes a user from a team.
     *
     * @param team UUID of the team
     * @param user UUID of the authenticated user unsubscribing
     * @return confirmation message
     */
    @DeleteMapping("/unsubscribeTeam/{team}")
    public ResponseEntity<String> unsubscribeTeam(
            @PathVariable UUID team,
            @AuthenticationPrincipal UUID user) {

        participationHandler.unsubscribeTeam(team, user);
        return ResponseEntity.ok("Team successfully unsubscribed");
    }
}
