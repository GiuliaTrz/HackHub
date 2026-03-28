package com.project.hackhub.repository;

import com.project.hackhub.model.hackathon.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, UUID> {




}
