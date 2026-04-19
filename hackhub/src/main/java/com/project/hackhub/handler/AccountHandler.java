package com.project.hackhub.handler;

import com.project.hackhub.model.utente.Anagrafica;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final UtenteRegistratoRepository utenteRepository;

    /**
     * Crea un nuovo account a partire dai dati anagrafici.
     * @param anagrafica dati del nuovo utente
     * @return l'utente creato e salvato
     * @throws IllegalArgumentException se i dati non sono validi (es. email già esistente)
     *
     * @author Giulia Trozzi
     */
    public UtenteRegistrato createAccount(Anagrafica anagrafica) {
        // Validazione campi obbligatori
        if (anagrafica.getEmail() == null || anagrafica.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (anagrafica.getUserName() == null || anagrafica.getUserName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        // Verifica unicità email (supponendo che l'email sia univoca)
        if (utenteRepository.existsByAnagrafica_Email(anagrafica.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        UtenteRegistrato nuovoUtente = new UtenteRegistrato(anagrafica);
        return utenteRepository.save(nuovoUtente);
    }

    /**
     * Modifica i dati anagrafici di un utente esistente.
     * @param userId ID dell'utente da modificare (deve corrispondere all'utente loggato o avere permessi admin)
     * @param nuovaAnagrafica nuovi dati
     * @return utente aggiornato
     *  @author Giulia Trozzi
     */
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
    public void deleteAccount(UUID userId) {
        if (!utenteRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        //eventuali controlli di permessi (solo admin o l'utente stesso)
        utenteRepository.deleteById(userId);
    }
}