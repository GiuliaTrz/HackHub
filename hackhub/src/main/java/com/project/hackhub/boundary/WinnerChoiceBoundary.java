package com.project.hackhub.boundary;

import com.project.hackhub.handler.WinnerChoiceHandler;
import com.project.hackhub.model.team.Team;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/winner")
public class WinnerChoiceBoundary {
    private final WinnerChoiceHandler winnerChoiceHandler;

    public WinnerChoiceBoundary(WinnerChoiceHandler winnerChoiceHandler){
        this.winnerChoiceHandler = winnerChoiceHandler;
    }

    @PatchMapping("/{hackathonId}")
    public ResponseEntity<String> proclaimWinner(
            @PathVariable UUID hackathonId,
            @AuthenticationPrincipal UUID organizerId,
            @RequestBody UUID teamId) {

        Team winner = winnerChoiceHandler.proclaimWinner(teamId, organizerId, hackathonId);
        return ResponseEntity.ok("Winner team is " + winner.getName());
    }

    @GetMapping("/{hackathonId}/allTeams")
    public ResponseEntity<List<UUID>> getAllTeams(
            @AuthenticationPrincipal UUID organizerId,
            @PathVariable UUID hackathonId) {

        return ResponseEntity.ok(winnerChoiceHandler.getAllTeams(organizerId, hackathonId));
    }

}
