package com.project.hackhub.handler;

import com.project.hackhub.dto.AnagraficaDTO;
import com.project.hackhub.model.utente.Anagrafica;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final PasswordEncoder passwordEncoder;
    private final UtenteRegistratoRepository utenteRepository;

    /**
     * Crea un nuovo account a partire dai dati anagrafici.
     *
     * @param anagraficaDto dati del nuovo utente
     * @throws IllegalArgumentException se i dati non sono validi (es. email già esistente)
     * @author Giulia Trozzi
     */
    @Transactional
    public void createAccount(AnagraficaDTO anagraficaDto) {

        // Validazione campi obbligatori
        if (anagraficaDto.email() == null || anagraficaDto.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (anagraficaDto.userName() == null || anagraficaDto.userName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        // Verifica unicità email (supponendo che l'email sia univoca)
        if (utenteRepository.existsByAnagrafica_Email(anagraficaDto.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Anagrafica anagrafica = new Anagrafica(
                anagraficaDto.userName(),
                anagraficaDto.userSurname(),
                anagraficaDto.fiscalCode(),
                anagraficaDto.address(),
                anagraficaDto.email()
        );

        UtenteRegistrato newUser = new UtenteRegistrato(anagrafica, passwordEncoder.encode(anagraficaDto.password()));
        utenteRepository.save(newUser);
    }

    /**
     * Modifica i dati anagrafici di un utente esistente.
     * @param userId ID dell'utente da modificare (deve corrispondere all'utente loggato o avere permessi admin)
     * @param nuovaAnagrafica nuovi dati
     * @return utente aggiornato
     *  @author Giulia Trozzi
     */
    @Transactional
    public UtenteRegistrato updateAccount(UUID userId, Anagrafica nuovaAnagrafica) {
        UtenteRegistrato utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        //l'utente può modificare solo se stesso oppure è admin (aggiungere logica)
        utente.setAnagrafica(nuovaAnagrafica);
        return utenteRepository.save(utente);
    }

    /**
     * Elimina un account.
     * @param userId ID dell'utente da eliminare
     *@author Giulia Trozzi
     */
    @Transactional
    public void deleteAccount(UUID userId) {
        if (!utenteRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        //eventuali controlli di permessi (solo admin o l'utente stesso)
        utenteRepository.deleteById(userId);
    }
}