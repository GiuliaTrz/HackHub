package com.project.hackhub.boundary;

import com.project.hackhub.dto.AuthResponse;
import com.project.hackhub.dto.LoginDTO;
import com.project.hackhub.handler.AuthenticationHandler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for user authentication operations.
 * Handles user login and authentication token generation.
 */
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/authentication")
public class AuthenticationBoundary {

    private final AuthenticationHandler authenticationHandler;

    /**
     * Authenticates a user with provided credentials.
     *
     * @param dto DTO containing login credentials
     * @return ResponseEntity with authentication response including JWT token
     */
    @PostMapping()
    public ResponseEntity<AuthResponse> authenticateUser(
            @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authenticationHandler.authenticateUser(dto));
    }
}
