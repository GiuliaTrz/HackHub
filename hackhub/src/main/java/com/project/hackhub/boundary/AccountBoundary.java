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

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/account")
public class AccountBoundary {

    private final AccountHandler accountHandler;

    /**
     * Registrazione di un nuovo account.
     */
    @PostMapping("/registration")
    public ResponseEntity<Void> createAccount(
            @Valid @RequestBody AnagraficaDTO anagraficaDto) {
        accountHandler.createAccount(anagraficaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Modifica i dati del proprio account.
     */
    @PutMapping
    public ResponseEntity<Void> updateAccount(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody AnagraficaDTO dto) {
        // Converte DTO in Anagrafica
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
     * Elimina il proprio account.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal UUID userId) {
        accountHandler.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }
}