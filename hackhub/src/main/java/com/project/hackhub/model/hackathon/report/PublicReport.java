package com.project.hackhub.model.hackathon.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

/**
 * Publicly visible report view with restricted information.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PublicReport extends Report {

    private final Map<Team, Float> teamsGrades;

    public PublicReport(String name, String ruleBook, Prenotazione reservation, LocalDate expiredSubscriptionsDate, HackathonState state, Soldi moneyPrize, int maxTeamDimension, Team winner, Map<Team, Float> teamsGrades) {
        super(name, ruleBook, reservation, expiredSubscriptionsDate, state, moneyPrize, maxTeamDimension, winner);
        this.teamsGrades = teamsGrades;
    }
}
