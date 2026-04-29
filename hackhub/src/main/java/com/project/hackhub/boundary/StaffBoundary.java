package com.project.hackhub.boundary;

import com.project.hackhub.handler.HackathonHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
@AllArgsConstructor
public class StaffBoundary {

    private final HackathonHandler hackathonHandler;

    /**
     * Aggiunge un mentore all'hackathon.
     */
    @PostMapping("/{hackathonId}/mentors")
    public ResponseEntity<Void> addMentor(
            @AuthenticationPrincipal UUID editorId,
            @PathVariable UUID hackathonId,
            @RequestBody UUID mentorId) {
        hackathonHandler.modifyStaff(editorId, hackathonId, mentorId, "MENTOR", true);
        return ResponseEntity.ok().build();
    }

    /**
     * Rimuove un mentore dall'hackathon.
     */
    @DeleteMapping("/{hackathonId}/mentors/{mentorId}")
    public ResponseEntity<Void> removeMentor(
            @AuthenticationPrincipal UUID editorId,
            @PathVariable UUID hackathonId,
            @PathVariable UUID mentorId) {
        hackathonHandler.modifyStaff(editorId, hackathonId, mentorId, "MENTOR", false);
        return ResponseEntity.ok().build();
    }
}