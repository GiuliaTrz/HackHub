package com.project.hackhub.boundary;

import com.project.hackhub.handler.TeamPartecipationHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Boundary for team participation actions.
 * Allows a user to voluntarily leave a team.
 */
@RestController
@RequestMapping("/api/team-participation")
@AllArgsConstructor
public class TeamParticipationBoundary {

    private final TeamPartecipationHandler teamParticipationHandler;

    /**
     * Allows the authenticated user to leave a team.
     *
     * @param userId the authenticated user leaving the team
     * @param teamId the ID of the team to leave
     * @return a {@link ResponseEntity} with status 200 OK
     * @throws IllegalArgumentException      if the user or team does not exist
     * @throws UnsupportedOperationException if the user is the team leader or the team has only one member
     */
    @PostMapping("/{teamId}/leave")
    public ResponseEntity<Void> leaveTeam(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID teamId) {
        teamParticipationHandler.leaveTeam(userId, teamId);
        return ResponseEntity.ok().build();
    }
}