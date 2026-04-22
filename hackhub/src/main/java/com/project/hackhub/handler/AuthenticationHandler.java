package com.project.hackhub.handler;

import com.project.hackhub.dto.AuthResponse;
import com.project.hackhub.dto.LoginDTO;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import com.project.hackhub.service.ServiceJwt;
import lombok.AllArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationHandler {

    private final UtenteRegistratoRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceJwt serviceJwt;

    @Transactional
    public AuthResponse authenticateUser(LoginDTO dto) {
        UtenteRegistrato user = userRepository.findByAnagrafica_UserName(dto.userName())
                .orElseThrow(() -> new IllegalArgumentException("userName invalid"));
        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash()))
            throw new IllegalArgumentException("Password not valid");
        String token = serviceJwt.generateToken(user);
        return new AuthResponse(token, "Bearer");
    }
}
