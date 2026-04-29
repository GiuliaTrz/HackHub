package com.project.hackhub.boundary;

import com.project.hackhub.handler.TeamPartecipationHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/team-participation")
@AllArgsConstructor
public class TeamParticipationBoundary {

    private final TeamPartecipationHandler teamParticipationHandler;

    @PostMapping("/{teamId}/leave")
    public ResponseEntity<Void> leaveTeam(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID teamId) {
        teamParticipationHandler.leaveTeam(userId, teamId);
        return ResponseEntity.ok().build();
    }
}