package com.project.hackhub.repository;

import com.project.hackhub.model.hackathon.IntervalloTemporale;
import com.project.hackhub.model.hackathon.Localita;
import com.project.hackhub.model.hackathon.builder.HackathonBuilderMemento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrenotazioneRepository extends JpaRepository<HackathonBuilderMemento, UUID> {

    boolean existsByLocationAndDataRange(Localita l, IntervalloTemporale i);
}
