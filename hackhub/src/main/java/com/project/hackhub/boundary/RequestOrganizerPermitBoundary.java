package com.project.hackhub.boundary;

import com.project.hackhub.handler.RequestOrganizerPermitHandler;
import com.project.hackhub.model.team.FileTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizer/permit")
@RequiredArgsConstructor
public class RequestOrganizerPermitBoundary {

    private final RequestOrganizerPermitHandler requestOrganizerPermitHandler;

    @PostMapping("/request")
    public ResponseEntity<String> requestPermission(
            @AuthenticationPrincipal UUID user,
            @RequestBody FileTemplate f) {

        requestOrganizerPermitHandler.requestPermission(user, f);
        return ResponseEntity.ok("permesso garantito!");
    }
}
