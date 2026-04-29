package com.project.hackhub.handler;

import com.project.hackhub.dto.AuthResponse;
import com.project.hackhub.dto.LoginDTO;
import com.project.hackhub.model.user.User;
import com.project.hackhub.repository.UserRepository;
import com.project.hackhub.service.ServiceJwt;
import lombok.AllArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceJwt serviceJwt;

    @Transactional
    public AuthResponse authenticateUser(LoginDTO dto) {
        User user = userRepository.findByPersonalData_UserName(dto.userName())
                .orElseThrow(() -> new IllegalArgumentException("userName invalid"));
        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash()))
            throw new IllegalArgumentException("Password not valid");
        String token = serviceJwt.generateToken(user);
        return new AuthResponse(token, "Bearer");
    }
}
