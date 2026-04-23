package com.project.hackhub.boundary;

import com.project.hackhub.handler.TeamHandler;
import com.project.hackhub.model.team.Team;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Boundary class for team-related operations in the HackHub application.
 * This REST controller handles HTTP requests related to teams.
 */
@RestController
@RequestMapping("/api/team")
@AllArgsConstructor
public class TeamBoundary {

    private final TeamHandler teamHandler;

    /**
     * Creates a new team for a given hackathon.
     *
     * @param creatorId the ID of the user creating the team
     * @param hackathonId the ID of the hackathon for which the team is created
     * @param teamName the name of the team to be created
     * @return a ResponseEntity with status CREATED if successful
     */
    @PostMapping("/{hackathonId}/creation")
    public ResponseEntity<Void> createTeam(
            @AuthenticationPrincipal UUID creatorId,
            @PathVariable UUID hackathonId,
            @RequestBody String teamName) {
        teamHandler.createTeam(creatorId, hackathonId, teamName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
