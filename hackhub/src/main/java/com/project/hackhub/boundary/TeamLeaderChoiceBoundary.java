package com.project.hackhub.boundary;

import com.project.hackhub.handler.TeamLeaderChoiceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing team leader changes.
 * Handles selection and assignment of a new team leader.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teamLeader")
public class TeamLeaderChoiceBoundary {

    private final TeamLeaderChoiceHandler teamLeaderChoiceHandler;

    /**
     * Changes the team leader to a new member.
     *
     * @param newLeader UUID of the user to become the new team leader
     * @param oldLeader UUID of the authenticated current team leader
     * @param t UUID of the team
     * @return confirmation message with new leader information
     */
    @PatchMapping("/{t}/choice")
    public ResponseEntity<String> chooseNewTeamLeader(
            @RequestBody UUID newLeader,
            @AuthenticationPrincipal UUID oldLeader,
            @PathVariable UUID t) {

        teamLeaderChoiceHandler.chooseNewTeamLeader(newLeader, oldLeader, t);
        return ResponseEntity.ok("New team leader: " + newLeader);
    }
}
