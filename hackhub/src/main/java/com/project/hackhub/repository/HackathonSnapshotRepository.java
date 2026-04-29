package com.project.hackhub.repository;

import com.project.hackhub.model.hackathon.builder.HackathonSnapshot;
import com.project.hackhub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HackathonSnapshotRepository extends JpaRepository<HackathonSnapshot, UUID> {

    Optional<HackathonSnapshot> findByAuthor(User coordinator);
}
