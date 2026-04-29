package com.project.hackhub.boundary;

import com.project.hackhub.dto.AnagraficaDTO;
import com.project.hackhub.handler.AccountHandler;
import com.project.hackhub.model.utente.Anagrafica;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Boundary for account management.
 * Handles registration, update and deletion of user accounts.
 */
@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/account")
public class AccountBoundary {

    private final AccountHandler accountHandler;

    /**
     * Registers a new user account.
     *
     * @param anagraficaDto the validated personal data of the new user
     * @return a {@link ResponseEntity} with status 201 CREATED
     * @throws IllegalArgumentException if the email is already registered or required fields are missing
     */
    @PostMapping("/registration")
    public ResponseEntity<Void> createAccount(
            @Valid @RequestBody AnagraficaDTO anagraficaDto) {
        accountHandler.createAccount(anagraficaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates the personal data of the authenticated user.
     *
     * @param userId the authenticated user ID
     * @param dto    the new personal data
     * @return a {@link ResponseEntity} with status 200 OK
     * @throws IllegalArgumentException if the user does not exist
     */
    @PutMapping
    public ResponseEntity<Void> updateAccount(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody AnagraficaDTO dto) {
        Anagrafica nuovaAnagrafica = new Anagrafica(
                dto.userName(),
                dto.userSurname(),
                dto.fiscalCode(),
                dto.address(),
                dto.email()
        );
        accountHandler.updateAccount(userId, nuovaAnagrafica);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes the authenticated user's account.
     *
     * @param userId the authenticated user ID
     * @return a {@link ResponseEntity} with status 204 NO CONTENT
     * @throws IllegalArgumentException if the user does not exist
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal UUID userId) {
        accountHandler.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }
}