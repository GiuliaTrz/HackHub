package com.project.hackhub.repository;

import com.project.hackhub.model.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    @Query("SELECT t from Team t where t.hackathon.id = :hackathonId " +
            "AND t.grade = (SELECT MAX(t2.grade) FROM Team t2 WHERE t2.hackathon.id = :hackathonId) ")
    List<Team> getTeamsWithMaxGrade(@Param("hackathonId") UUID hackathonId);
}
