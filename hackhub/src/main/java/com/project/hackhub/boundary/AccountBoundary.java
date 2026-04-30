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

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/account")
public class AccountBoundary {

    private final AccountHandler accountHandler;

    // 1. Registrazione nuovo account
    @PostMapping("/registration")
    public ResponseEntity<Void> createAccount(@Valid @RequestBody PersonalDataDTO personalDataDto) {
        accountHandler.createAccount(personalDataDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. Aggiornamento dati profilo (Corretto il tipo di ritorno e la logica)
    @PutMapping("/update")
    public ResponseEntity<String> updateAccount(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody PersonalData personalData) {

        User updated = accountHandler.updateAccount(userId, personalData);
        return ResponseEntity.ok("updated successfully");
    }

    // 3. Eliminazione account
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal UUID userId) {
        accountHandler.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }
}