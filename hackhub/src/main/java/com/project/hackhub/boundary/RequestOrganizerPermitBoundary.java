package com.project.hackhub.boundary;

import com.project.hackhub.handler.RequestOrganizerPermitHandler;
import com.project.hackhub.model.team.FileTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing organizer permission requests.
 * Handles requests from users to obtain hackathon organizing privileges.
 */
@RestController
@RequestMapping("/api/organizer/permit")
@RequiredArgsConstructor
public class RequestOrganizerPermitBoundary {

    private final RequestOrganizerPermitHandler requestOrganizerPermitHandler;

    /**
     * Requests permission to organize hackathons.
     *
     * @param user UUID of the authenticated user requesting permission
     * @param f file template with organizer qualification details
     * @return confirmation message
     */
    @PatchMapping("/request")
    public ResponseEntity<String> requestPermission(
            @AuthenticationPrincipal UUID user,
            @RequestBody FileTemplate f) {

        requestOrganizerPermitHandler.requestPermission(user, f);
        return ResponseEntity.ok("Permission to organize hackathon granted!");
    }
}
