package com.project.hackhub.boundary;

import com.project.hackhub.dto.InfractionDTO;
import com.project.hackhub.handler.InfractionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/infraction")
@RequiredArgsConstructor
public class InfractionBoundary {

    private final InfractionHandler infractionHandler;

    @PostMapping("/report")
    public ResponseEntity<Void> reportInfraction(
            @AuthenticationPrincipal UUID mentor,
            @RequestBody InfractionDTO dto) {

        infractionHandler.reportInfraction(mentor, dto);
        return ResponseEntity.ok().build();
    }




}
