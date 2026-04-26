package com.project.hackhub.boundary;

import com.project.hackhub.handler.RequestOrganizerPermitHandler;
import com.project.hackhub.model.team.FileTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizer/permit")
@RequiredArgsConstructor
public class RequestOrganizerPermitBoundary {

    private final RequestOrganizerPermitHandler requestOrganizerPermitHandler;

    @PatchMapping("/request")
    public ResponseEntity<String> requestPermission(
            @AuthenticationPrincipal UUID user,
            @RequestBody FileTemplate f) {

        requestOrganizerPermitHandler.requestPermission(user, f);
        return ResponseEntity.ok("permission to organize hackathon granted!");
    }
}
