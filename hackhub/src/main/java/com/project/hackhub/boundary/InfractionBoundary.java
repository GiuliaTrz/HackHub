package com.project.hackhub.boundary;

import com.project.hackhub.dto.InfractionDTO;
import com.project.hackhub.handler.InfractionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/infraction")
@RequiredArgsConstructor
public class InfractionBoundary {

    private final InfractionHandler infractionHandler;

    @PostMapping("/report")
    public ResponseEntity<Void> reportInfraction(
            @AuthenticationPrincipal UUID mentor,
            @RequestBody InfractionDTO dto) {

        infractionHandler.reportInfraction(mentor, dto);
        return ResponseEntity.ok().build();
    }

     @DeleteMapping("/{team}/expel")
    public ResponseEntity<String> expelTeam(
            @AuthenticationPrincipal UUID coordinator,
            @PathVariable UUID team) {

        infractionHandler.expelTeam(team, coordinator);
        return ResponseEntity.ok("team has been successfully expelled");
    }

    @PatchMapping("/{team}/penalize")
    public ResponseEntity<String> penalizeTeam(
            @AuthenticationPrincipal UUID coordinator,
            @PathVariable UUID team,
            @RequestBody float pointsToDeduct) {

        infractionHandler.penalizeTeam(coordinator, team, pointsToDeduct);
        return ResponseEntity.ok("team will be penalized subtracting " + pointsToDeduct + " points from final grade");
    }

    @PostMapping("/handle")
    public ResponseEntity<String> handleInfraction(
            @AuthenticationPrincipal UUID coordinator,
            @RequestBody UUID team) {

        infractionHandler.handleInfraction(coordinator, team);
        return ResponseEntity.ok("Infraction to be handled: please, penalize or expel team");
    }

}
