package com.project.hackhub.repository;

import com.project.hackhub.model.hackathon.builder.HackathonBuilderMemento;
import com.project.hackhub.model.utente.UtenteRegistrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HackathonBuilderMementoRepository extends JpaRepository<HackathonBuilderMemento, UUID> {

    Optional<HackathonBuilderMemento> findByAuthor(UtenteRegistrato coordinator);

    void removeHackathonBuilderMementoByAuthor(UtenteRegistrato coordinator);
}
