package com.project.hackhub.boundary;

import com.project.hackhub.handler.PartecipationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teamPartecipation")
public class PartecipationBoundary {

    private final PartecipationHandler partecipationHandler;

    @DeleteMapping("/unsubscribeTeam/{team}")
    public ResponseEntity<String> unsubscribeTeam(
            @PathVariable UUID team,
            @AuthenticationPrincipal UUID user) {

        partecipationHandler.unsubscribeTeam(team, user);
        return ResponseEntity.ok("team desiscritto dall'hackathon a cui sta partecipando");
    }
}
