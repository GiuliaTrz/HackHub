package com.project.hackhub.repository;

import com.project.hackhub.model.team.Invito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvitoRepository extends JpaRepository<Invito, UUID> {
}
