package com.project.hackhub.boundary;

import com.project.hackhub.handler.PrizeHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/prize")
@AllArgsConstructor
public class PrizeBoundary {

    private final PrizeHandler prizeHandler;

    @PostMapping("/{hackathonId}/claim")
    public ResponseEntity<Void> claimPrize(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID hackathonId) {
        prizeHandler.claimPrize(userId, hackathonId);
        return ResponseEntity.ok().build();
    }
}