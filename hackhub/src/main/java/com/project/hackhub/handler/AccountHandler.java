package com.project.hackhub.handler;

import com.project.hackhub.dto.PersonalDataDTO;
import com.project.hackhub.model.user.PersonalData;
import com.project.hackhub.model.user.User;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository utenteRepository;

    /**
     * Crea un nuovo account a partire dai dati anagrafici.
     *
     * @param personalDataDto dati del nuovo utente
     * @throws IllegalArgumentException se i dati non sono validi (es. email già esistente)
     * @author Giulia Trozzi
     */
    @Transactional
    public void createAccount(PersonalDataDTO personalDataDto) {

        // Validazione campi obbligatori
        if (personalDataDto.email() == null || personalDataDto.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (personalDataDto.userName() == null || personalDataDto.userName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        // Verifica unicità email (supponendo che l'email sia univoca)
        if (utenteRepository.existsByPersonalData_Email(personalDataDto.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        PersonalData personalData = new PersonalData(
                personalDataDto.userName(),
                personalDataDto.userSurname(),
                personalDataDto.fiscalCode(),
                personalDataDto.address(),
                personalDataDto.email()
        );

        User newUser = new User(personalData, passwordEncoder.encode(personalDataDto.password()));
        utenteRepository.save(newUser);
    }

    /**
     * Modifica i dati anagrafici di un utente esistente.
     * @param userId ID dell'utente da modificare (deve corrispondere all'utente loggato o avere permessi admin)
     * @param nuovaPersonalData nuovi dati
     * @return utente aggiornato
     *  @author Giulia Trozzi
     */
    @Transactional
    public User updateAccount(UUID userId, PersonalData nuovaPersonalData) {
        User utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        //l'utente può modificare solo se stesso oppure è admin (aggiungere logica)
        utente.setPersonalData(nuovaPersonalData);
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