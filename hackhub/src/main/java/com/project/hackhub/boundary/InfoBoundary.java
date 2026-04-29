package com.project.hackhub.boundary;

import com.project.hackhub.handler.InfoHandler;
import com.project.hackhub.model.hackathon.report.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/info")
public class InfoBoundary {

    private final InfoHandler infoHandler;

    public InfoBoundary(InfoHandler infoHandler) {
        this.infoHandler = infoHandler;
    }

    /**
     * Endpoint for list of all hackathons
     * @return a list of all hackathons
     */
    @GetMapping("/hackathons")
    public ResponseEntity<List<UUID>> getAllHackathons() {
        return ResponseEntity.ok(infoHandler.getAllHackathons());
    }
    /**
     * Endpoint for getting a report of a given hackathon for a user based on hackathon's state
     * and the user's permissions
     * @param hackathonId
     * @param userId
     * @return a report
     */
    @GetMapping("/{hackathonId}/report")
    public ResponseEntity<Report> getHackathonReport(@PathVariable UUID hackathonId,
                                            @AuthenticationPrincipal UUID userId){
        return ResponseEntity.ok(infoHandler.getHackathonReport(hackathonId, userId));
    }
}
