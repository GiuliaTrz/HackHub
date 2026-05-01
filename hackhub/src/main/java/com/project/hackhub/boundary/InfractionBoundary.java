package com.project.hackhub.boundary;

import com.project.hackhub.dto.InfractionDTO;
import com.project.hackhub.handler.InfractionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing infractions within hackathon events.
 * Exposes endpoints for reporting, handling, penalizing, and deleting infractions.
 */
@RestController
@RequestMapping("api/infraction")
@RequiredArgsConstructor
public class InfractionBoundary {

    private final InfractionHandler infractionHandler;

    /**
     * Reports a new infraction for a team.
     *
     * @param mentor UUID of the authenticated mentor reporting the infraction
     * @param dto DTO containing infraction details
     * @return ResponseEntity with HTTP 200 OK status
     */
    @PostMapping("/report")
    public ResponseEntity<String> reportInfraction(
            @AuthenticationPrincipal UUID mentor,
            @RequestBody InfractionDTO dto) {

        infractionHandler.reportInfraction(mentor, dto);
        return ResponseEntity.ok("infraction has been successfully reported");
    }

    /**
     * Expels a team from the hackathon.
     *
     * @param coordinator UUID of the authenticated coordinator
     * @param team UUID of the team to expel
     * @return confirmation message
     */
    @DeleteMapping("/{team}/expel")
    public ResponseEntity<String> expelTeam(
            @AuthenticationPrincipal UUID coordinator,
            @PathVariable UUID team) {

        infractionHandler.expelTeam(team, coordinator);
        return ResponseEntity.ok("Team has been successfully expelled");
    }

    /**
     * Penalizes a team by deducting a specified number of points.
     *
     * @param coordinator UUID of the authenticated coordinator
     * @param team UUID of the team to penalize
     * @param pointsToDeduct number of points to deduct
     * @return confirmation message
     */
    @PatchMapping("/{team}/penalize")
    public ResponseEntity<String> penalizeTeam(
            @AuthenticationPrincipal UUID coordinator,
            @PathVariable UUID team,
            @RequestBody float pointsToDeduct) {

        infractionHandler.penalizeTeam(coordinator, team, pointsToDeduct);
        return ResponseEntity.ok("Team will be penalized by deducting " + pointsToDeduct + " points from final grade");
    }

    /**
     * Handles an infraction for a team.
     * The coordinator can then decide whether to penalize or expel the team.
     *
     * @param coordinator UUID of the authenticated coordinator
     * @param team UUID of the team with infraction
     * @return informational message
     */
    @PostMapping("/handle")
    public ResponseEntity<String> handleInfraction(
            @AuthenticationPrincipal UUID coordinator,
            @RequestBody UUID team) {

        infractionHandler.handleInfraction(coordinator, team);
        return ResponseEntity.ok("Infraction to be handled: please penalize or expel the team");
    }

    /**
     * Deletes a specific infraction from a hackathon.
     *
     * @param userId UUID of the authenticated user
     * @param hackathonId UUID of the hackathon
     * @param infractionIndex index of the infraction to remove
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{hackathonId}/{infractionIndex}")
    public ResponseEntity<String> deleteInfraction(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID hackathonId,
            @PathVariable int infractionIndex) {

        infractionHandler.deleteInfraction(userId, hackathonId, infractionIndex);
        return ResponseEntity.ok("Infraction deleted");
    }
}