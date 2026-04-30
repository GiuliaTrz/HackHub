package com.project.hackhub.boundary;

import com.project.hackhub.dto.PersonalDataDTO;
import com.project.hackhub.handler.AccountHandler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing user account operations.
 * Exposes endpoints for account registration and management.
 */
@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/account")
public class AccountBoundary {

    private final AccountHandler accountHandler;

    /**
     * Creates a new user account with the provided personal data.
     *
     * @param personalDataDto DTO containing user personal information
     * @return ResponseEntity with HTTP 201 Created status
     */
    @PostMapping("/registration")
    public ResponseEntity<Void> createAccount(
            @Valid @RequestBody PersonalDataDTO personalDataDto){
        accountHandler.createAccount(personalDataDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}