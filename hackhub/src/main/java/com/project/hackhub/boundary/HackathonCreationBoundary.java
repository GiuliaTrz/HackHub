package com.project.hackhub.boundary;

import com.project.hackhub.dto.HackathonCreationResponse;
import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.handler.HackathonCreationHandler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/hackathon")
@AllArgsConstructor
@Validated
public class HackathonCreationBoundary {

    private final HackathonCreationHandler hackathonCreationHandler;

    @PostMapping("/creation")
    public ResponseEntity<String> createHackathon(
            @AuthenticationPrincipal UUID coordinator,
            @Valid @RequestBody HackathonDTO dto) {

        HackathonCreationResponse result =
                hackathonCreationHandler.createHackathon(dto, coordinator);

        if (result.created()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result.message());
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(result.message());
        }
    }
}
