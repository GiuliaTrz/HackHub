package com.project.hackhub.boundary;

import com.project.hackhub.handler.TeamHandler;
import com.project.hackhub.model.team.Team;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Boundary for team-related operations.
 * Allows creation, modification and member removal of teams.
 */
@RestController
@RequestMapping("/api/team")
@AllArgsConstructor
public class TeamBoundary {

    private final TeamHandler teamHandler;

    /**
     * Creates a new team for a given hackathon.
     *
     * @param creatorId   the authenticated user creating the team
     * @param hackathonId the ID of the hackathon
     * @param teamName    the name of the new team
     * @return a {@link ResponseEntity} with status 201 CREATED
     * @throws IllegalArgumentException      if the team name is blank or already exists in the hackathon
     * @throws UnsupportedOperationException if the user does not have permission to create a team
     */
    @PostMapping("/{hackathonId}/creation")
    public ResponseEntity<Void> createTeam(
            @AuthenticationPrincipal UUID creatorId,
            @PathVariable UUID hackathonId,
            @RequestBody String teamName) {
        teamHandler.createTeam(creatorId, hackathonId, teamName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates the name of a team.
     *
     * @param editorId the authenticated user (must be team leader or organizer)
     * @param teamId   the ID of the team
     * @param newName  the new team name
     * @return a {@link ResponseEntity} containing the updated {@link Team}
     * @throws UnsupportedOperationException if the user is neither team leader nor organizer
     */
    @PatchMapping("/{teamId}")
    public ResponseEntity<String> updateTeam(
            @AuthenticationPrincipal UUID editorId,
            @PathVariable UUID teamId,
            @RequestBody String newName) {
        teamHandler.updateTeam(editorId, teamId, newName);
        return ResponseEntity.ok("team has been successfully updated");
    }

    /**
     * Removes a member from a team.
     *
     * @param requesterId the authenticated user (team leader or organizer)
     * @param teamId      the ID of the team
     * @param memberId    the ID of the member to remove
     * @return a {@link ResponseEntity} with status 200 OK
     * @throws UnsupportedOperationException if the user lacks permission or tries to remove the team leader
     * @throws IllegalStateException         if the member is not part of the team
     */
    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<String> removeMember(
            @AuthenticationPrincipal UUID requesterId,
            @PathVariable UUID teamId,
            @PathVariable UUID memberId) {
        teamHandler.removeMember(requesterId, teamId, memberId);
        return ResponseEntity.ok("Member removed from team");
    }
}