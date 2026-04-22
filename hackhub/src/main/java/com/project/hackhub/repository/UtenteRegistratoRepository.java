package com.project.hackhub.repository;

import com.project.hackhub.model.utente.UtenteRegistrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRegistratoRepository extends JpaRepository<UtenteRegistrato, UUID> {

    boolean existsByAnagrafica_Email(String email);

    Optional<UtenteRegistrato> findByAnagrafica_UserName(String anagraficaUserName);
}