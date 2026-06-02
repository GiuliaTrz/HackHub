package com.project.hackhub.repository;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    @Query("SELECT s FROM Submission s WHERE s.hackathon = :hackathon " +
            "AND s.timestamp = (SELECT MAX(sub2.timestamp) FROM Submission sub2 " +
            "WHERE sub2.hackathon = :hackathon AND sub2.team = s.team)")
    List<Submission> findLatestSubmissionsByHackathon(@Param("hackathon") Hackathon hackathon);
}
