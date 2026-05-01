package com.project.hackhub.boundary;

import com.project.hackhub.handler.PrizeHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Boundary for prize redemption.
 * Allows a member of the winning team to claim their share of the prize money.
 */
@RestController
@RequestMapping("/api/prize")
@AllArgsConstructor
public class PrizeBoundary {

    private final PrizeHandler prizeHandler;

    /**
     * Redeems the prize money for the authenticated user in a concluded hackathon.
     *
     * @param userId      the authenticated user (must belong to the winning team)
     * @param hackathonId the ID of the concluded hackathon
     * @return a {@link ResponseEntity} with status 200 OK
     * @throws IllegalStateException         if the hackathon is not concluded or has no winner
     * @throws UnsupportedOperationException if the user is not in the winning team
     */
    @PostMapping("/{hackathonId}/claim")
    public ResponseEntity<String> claimPrize(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID hackathonId) {
        prizeHandler.claimPrize(userId, hackathonId);
        return ResponseEntity.ok("Prize claimed successfully");
    }
}