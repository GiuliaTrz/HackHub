package com.project.hackhub.boundary;

import com.project.hackhub.dto.PersonalDataDTO;
import com.project.hackhub.handler.AccountHandler;
import com.project.hackhub.model.user.PersonalData;
import com.project.hackhub.model.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<Void> createAccount(@Valid @RequestBody PersonalDataDTO personalDataDto) {
        accountHandler.createAccount(personalDataDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAccount(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody PersonalData personalData) {

        User updated = accountHandler.updateAccount(userId, personalData);
        return ResponseEntity.ok("updated successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal UUID userId) {
        accountHandler.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }
}