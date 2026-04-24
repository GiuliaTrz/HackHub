package com.project.hackhub.repository;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, UUID> {

    @Query("SELECT h FROM Hackathon h WHERE h.expiredSubscriptionsDate < :now\n AND h.stateType = HackathonStateType.IN_ISCRIZIONE")
    List<Hackathon> getExpiredSubscriptions(@Param("now") LocalDateTime now);

    @Query("SELECT h FROM Hackathon h WHERE h.reservation.timeInterval.endDate < :now\n AND h.stateType = HackathonStateType.IN_CORSO")
    List<Hackathon> getExpiredSubmissions(LocalDateTime now);

    @Query(" SELECT i FROM Hackathon h JOIN h.infractions i WHERE h = :hackathon AND i.iTeam = :team")
    Optional<Infraction> findInfractionByTeam(@Param("hackathon") Hackathon h,
                                              @Param("team") Team t);
    @Query("SELECT t FROM Hackathon h JOIN h.teamsList t WHERE t.id = :team")
    Optional<Team> findByTeamId(@Param("team")UUID team);
}
