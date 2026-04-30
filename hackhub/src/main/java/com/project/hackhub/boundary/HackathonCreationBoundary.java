package com.project.hackhub.boundary;

import com.project.hackhub.dto.HackathonCreationResponse;
import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.dto.TaskDTO;
import com.project.hackhub.handler.HackathonCreationHandler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for hackathon creation and task management.
 * Handles creation of hackathon events and insertion of associated tasks.
 */
@RestController
@RequestMapping("/api/hackathon")
@AllArgsConstructor
@Validated
public class HackathonCreationBoundary {

    private final HackathonCreationHandler hackathonCreationHandler;

    /**
     * Creates a new hackathon event with the provided details.
     *
     * @param coordinator UUID of the authenticated coordinator creating the hackathon
     * @param dto DTO containing hackathon details (location, name, dates, etc.)
     * @return ResponseEntity with HTTP 201 Created if successful, HTTP 200 OK if suspended
     */
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

    /**
     * Inserts a new task into an existing hackathon.
     *
     * @param coordinator UUID of the authenticated coordinator
     * @param taskDTO DTO containing task details
     * @param hackathonId UUID of the hackathon
     * @return ResponseEntity with HTTP 201 Created status
     */
    @PostMapping("/{hackathonId}/task")
    public ResponseEntity<Void> insertTask(
            @AuthenticationPrincipal UUID coordinator,
            @RequestBody TaskDTO taskDTO,
            @PathVariable UUID hackathonId) {

        hackathonCreationHandler.insertTask(coordinator, taskDTO, hackathonId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
