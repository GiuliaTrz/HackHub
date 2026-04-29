package com.project.hackhub.model.hackathon.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.hackhub.model.hackathon.Reservation;
import com.project.hackhub.model.hackathon.Money;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 *Detailed report view intended for users that have TEAM permission.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DetailedReport extends Report {

    @JsonIgnoreProperties({"passwordHash", "organizer"})
    private final User coordinator;

    @JsonIgnoreProperties({"passwordHash", "organizer"})
    private final User judge;

    @JsonIgnoreProperties({"passwordHash", "organizer"})
    private final List<User> mentors;

    public DetailedReport(String name, String ruleBook, Reservation reservation, LocalDate expiredSubscriptionsDate, HackathonState state, Money moneyPrize, int maxTeamDimension, Team winner, User coordinator, User judge, List<User> mentors) {
        super(name, ruleBook, reservation, expiredSubscriptionsDate, state, moneyPrize, maxTeamDimension, winner);
        this.coordinator = coordinator;
        this.judge = judge;
        this.mentors = mentors;
    }
}