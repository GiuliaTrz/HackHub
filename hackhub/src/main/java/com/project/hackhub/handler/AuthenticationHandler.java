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
    public AuthResponse attivaAutenticazione(LoginDTO dto) {
        UtenteRegistrato utente = userRepository.findByAnagrafica_UserName(dto.username())
                .orElseThrow(() -> new IllegalArgumentException("username invalid"));
        if (!passwordEncoder.matches(dto.password(), utente.getPasswordHash()))
            throw new IllegalArgumentException("Password not valid");
        String token = serviceJwt.generateToken(utente);
        return new AuthResponse(token, "Bearer");
    }
}
