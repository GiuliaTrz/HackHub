package com.project.hackhub.boundary;

import com.project.hackhub.handler.TeamLeaderChoiceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teamLeader")
public class TeamLeaderChoiceBoundary {

    private final TeamLeaderChoiceHandler teamLeaderChoiceHandler;

    @PatchMapping("/{t}/choice")
    public ResponseEntity<String> chooseNewTeamLeader(
            @RequestBody UUID newLeader,
            @AuthenticationPrincipal UUID oldLeader,
            @PathVariable UUID t) {

        teamLeaderChoiceHandler.chooseNewTeamLeader(newLeader, oldLeader, t);
        return ResponseEntity.ok("nuovo team leader" + newLeader);
    }
}
