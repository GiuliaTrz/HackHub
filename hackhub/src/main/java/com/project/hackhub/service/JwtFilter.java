package com.project.hackhub.service;

import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final ServiceJwt servizioJwt;
    private final UtenteRegistratoRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token;
        UUID userId;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                userId = servizioJwt.extractUserId(token);
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UtenteRegistrato user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        servizioJwt.validateToken(token, user);
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,
                                null, Collections.emptyList());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }

}