package com.project.hackhub.boundary;

import com.project.hackhub.handler.StaffHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Boundary for staff-related operations.
 * Handles adding and removing mentors from a hackathon.
 */
@RestController
@RequestMapping("/api/staff")
@AllArgsConstructor
public class StaffBoundary {

    private final StaffHandler staffHandler;

    /**
     * Adds a mentor to a hackathon.
     *
     * @param organizerId  the authenticated organizer performing the action
     * @param hackathonId  the ID of the hackathon
     * @param mentorId     the ID of the user to add as mentor
     * @return a {@link ResponseEntity} with status 200 OK
     * @throws IllegalArgumentException      if any of the IDs do not correspond to an existing entity
     * @throws UnsupportedOperationException if the organizer lacks the required permissions
     * @throws IllegalStateException         if the mentor is not available for the hackathon
     */
    @PostMapping("/{hackathonId}/mentors")
    public ResponseEntity<Void> addMentor(
            @AuthenticationPrincipal UUID organizerId,
            @PathVariable UUID hackathonId,
            @RequestBody UUID mentorId) {
        staffHandler.addMentor(organizerId, hackathonId, mentorId);
        return ResponseEntity.ok().build();
    }

    /**
     * Removes a mentor from a hackathon.
     *
     * @param organizerId  the authenticated organizer performing the action
     * @param hackathonId  the ID of the hackathon
     * @param mentorId     the ID of the mentor to remove
     * @return a {@link ResponseEntity} with status 200 OK
     * @throws IllegalArgumentException      if the user is not a mentor of the hackathon
     * @throws UnsupportedOperationException if the organizer lacks the required permissions
     *
     */
    @DeleteMapping("/{hackathonId}/mentors/{mentorId}")
    public ResponseEntity<Void> removeMentor(
            @AuthenticationPrincipal UUID organizerId,
            @PathVariable UUID hackathonId,
            @PathVariable UUID mentorId) {
        staffHandler.removeMentor(organizerId, hackathonId, mentorId);
        return ResponseEntity.ok().build();
    }
}