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
     * Creates a new account from personal data.
     *
     * @param personalDataDto personal data of the new user
     * @throws IllegalArgumentException if the data is not valid (e.g. email already exists)
     * @author Giulia Trozzi
     */
    @Transactional
    public void createAccount(PersonalDataDTO personalDataDto) {

        // Validation of mandatory fields
        if (personalDataDto.email() == null || personalDataDto.email().isBlank()) {
             throw new IllegalArgumentException("Email is required");
         }
         if (personalDataDto.userName() == null || personalDataDto.userName().isBlank()) {
             throw new IllegalArgumentException("Name is required");
         }
         // Verify email uniqueness (assuming the email is unique)
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
     * Modifies personal data of an existing user.
     * @param userId ID of the user to modify (must correspond to the logged-in user or have admin permissions)
     * @param nuovaPersonalData new data
     * @author Giulia Trozzi
     */
    @Transactional
    public void updateAccount(UUID userId, PersonalData nuovaPersonalData) {
        User utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // the user can modify only themselves or is an admin (add logic)
        if(!nuovaPersonalData.getEmail().equals(utente.getPersonalData().getEmail()))
            throw new IllegalArgumentException("associated email cannot change");
        utente.setPersonalData(nuovaPersonalData);
        utenteRepository.save(utente);
    }

    /**
     * Deletes an account.
     * @param userId ID of the user to be deleted
     * @author Giulia Trozzi
     */
    @Transactional
    public void deleteAccount(UUID userId) {
        if (!utenteRepository.existsById(userId)) {
             throw new IllegalArgumentException("User not found");
         }
         // possible permission checks (only admin or the user themselves)
         utenteRepository.deleteById(userId);
    }
}