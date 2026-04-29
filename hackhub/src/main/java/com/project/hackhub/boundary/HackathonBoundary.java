package com.project.hackhub.boundary;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.handler.HackathonHandler;
import com.project.hackhub.handler.StaffHandler;
import com.project.hackhub.model.hackathon.Hackathon;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Boundary for hackathon management operations.
 * Handles deletion, modification of hackathon data and staff role changes.
 */
@RestController
@RequestMapping("/api/hackathon")
@AllArgsConstructor
public class HackathonBoundary {

    private final HackathonHandler hackathonHandler;
    private final StaffHandler staffHandler;

    /**
     * Deletes a hackathon.
     *
     * @param deleterId   the authenticated user (must have global delete permission)
     * @param hackathonId the ID of the hackathon to delete
     * @return a {@link ResponseEntity} with status 204 NO CONTENT
     * @throws IllegalArgumentException      if the user or hackathon does not exist
     * @throws UnsupportedOperationException if the user lacks permission
     */
    @DeleteMapping("/{hackathonId}")
    public ResponseEntity<Void> deleteHackathon(
            @AuthenticationPrincipal UUID deleterId,
            @PathVariable UUID hackathonId) {
        hackathonHandler.deleteHackathon(deleterId, hackathonId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the basic data of a hackathon. The reservation is never modified.
     *
     * @param editorId    the authenticated editor
     * @param hackathonId the ID of the hackathon
     * @param dto         the DTO containing the new data
     * @return a {@link ResponseEntity} with the updated {@link Hackathon}
     * @throws UnsupportedOperationException if the user lacks permission
     */
    @PutMapping("/{hackathonId}")
    public ResponseEntity<Hackathon> updateHackathon(
            @AuthenticationPrincipal UUID editorId,
            @PathVariable UUID hackathonId,
            @RequestBody HackathonDTO dto) {
        Hackathon updated = hackathonHandler.updateHackathon(editorId, hackathonId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Changes the role of a staff member inside a hackathon.
     *
     * @param organizerId  the authenticated organizer
     * @param hackathonId  the ID of the hackathon
     * @param request      contains the target user ID and the new role
     * @return a {@link ResponseEntity} with status 200 OK
     * @throws IllegalArgumentException      if the role is invalid
     * @throws UnsupportedOperationException if the organizer lacks permissions or tries to change their own role
     */
    @PostMapping("/{hackathonId}/staff/change-role")
    public ResponseEntity<Void> modifyStaff(
            @AuthenticationPrincipal UUID organizerId,
            @PathVariable UUID hackathonId,
            @RequestBody StaffRoleChangeRequest request) {
        staffHandler.changeStaffRole(organizerId, hackathonId,
                request.targetUserId(), request.newRole());
        return ResponseEntity.ok().build();
    }

    /**
     * DTO for a staff role change request.
     */
    public record StaffRoleChangeRequest(UUID targetUserId, String newRole) {}
}