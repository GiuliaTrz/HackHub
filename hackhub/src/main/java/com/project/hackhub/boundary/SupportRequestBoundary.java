package com.project.hackhub.boundary;

import com.project.hackhub.dto.AidRequestDTO;
import com.project.hackhub.handler.SupportRequestHandler;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.service.calendar.Slot;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/support")
@AllArgsConstructor
public class SupportRequestBoundary {
    private final SupportRequestHandler supportRequestHandler;

    /** As interaction with the external system Calendar is simulated,
     * inconsistencies can be expected during testing
    */
    @GetMapping("/available-slots/{hackathon}")
    public ResponseEntity<List<Slot>> getAvailableSlots(
        @AuthenticationPrincipal UUID user,
        @PathVariable UUID hackathon){
        return ResponseEntity.ok(supportRequestHandler.getAvailableSlots(user, hackathon));
    }

    /** As interaction with the external system Calendar is simulated,
     * inconsistencies can be expected during testing
     */
    @PostMapping("/propose-call/{team}")
    public ResponseEntity<String> proposeCall(
            @AuthenticationPrincipal UUID mentor,
            @RequestBody Slot slot,
            @PathVariable UUID team
    ){
        supportRequestHandler.proposeCall(mentor, slot, team);
        return ResponseEntity.ok("call proposed");
    }

    @PostMapping("/send-aid-request")
    public ResponseEntity<String> sendAidRequest(
            @AuthenticationPrincipal UUID leader,
            @RequestBody AidRequestDTO dto){
        supportRequestHandler.sendAidRequest(leader, dto);
        return ResponseEntity.ok("aid request sent");
    }
    @GetMapping("/{hackathonId}")
    public ResponseEntity<List<AidRequest>> getSupportRequests(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID hackathonId) {
        List<AidRequest> requests = supportRequestHandler.getAllSupportRequests(userId, hackathonId);
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/{hackathonId}/teams/{teamId}")
    public ResponseEntity<Void> deleteSupportRequest(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID hackathonId,
            @PathVariable UUID teamId) {
        supportRequestHandler.deleteSupportRequest(userId, hackathonId, teamId);
        return ResponseEntity.noContent().build();
    }
}
