package com.project.hackhub.model.hackathon.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.hackhub.model.hackathon.Reservation;
import com.project.hackhub.model.hackathon.Money;
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

    private final Map<String, Float> teamsGrades;

    public PublicReport(String name, String ruleBook, Reservation reservation, LocalDate expiredSubscriptionsDate, HackathonState state, Money moneyPrize, int maxTeamDimension, Team winner, Map<String, Float> teamsGrades) {
        super(name, ruleBook, reservation, expiredSubscriptionsDate, state, moneyPrize, maxTeamDimension, winner);
        this.teamsGrades = teamsGrades;
    }
}
