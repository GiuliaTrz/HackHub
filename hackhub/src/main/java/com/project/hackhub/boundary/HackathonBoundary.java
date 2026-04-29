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

@RestController
@RequestMapping("/api/hackathon")
@AllArgsConstructor
public class HackathonBoundary {

    private final HackathonHandler hackathonHandler;
    private final StaffHandler staffHandler;

    @DeleteMapping("/{hackathonId}")
    public ResponseEntity<Void> deleteHackathon(
            @AuthenticationPrincipal UUID deleterId,
            @PathVariable UUID hackathonId) {
        hackathonHandler.deleteHackathon(deleterId, hackathonId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{hackathonId}")
    public ResponseEntity<Hackathon> updateHackathon(
            @AuthenticationPrincipal UUID editorId,
            @PathVariable UUID hackathonId,
            @RequestBody HackathonDTO dto) {
        Hackathon updated = hackathonHandler.updateHackathon(editorId, hackathonId, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{hackathonId}/staff/change-role")
    public ResponseEntity<Void> modifyStaff(
            @AuthenticationPrincipal UUID organizerId,
            @PathVariable UUID hackathonId,
            @RequestBody StaffRoleChangeRequest request) {
        staffHandler.changeStaffRole(organizerId, hackathonId,
                request.targetUserId(), request.newRole());
        return ResponseEntity.ok().build();
    }

    public record StaffRoleChangeRequest(UUID targetUserId, String newRole) {}
}