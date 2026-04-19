package com.project.hackhub.repository;

import com.project.hackhub.model.hackathon.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
}
