package com.project.hackhub.model.hackathon.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import lombok.Getter;

import java.time.LocalDate;


/**
 * Abstracts class for all Hackathon report representations.
 *
 * <p>Extended by different final classes representing the same underlying data
 * exposed with different visibility levels depending on user permissions.</p>
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Report {

    private final String name;
    private final String ruleBook;
    private final Prenotazione reservation;
    private final LocalDate expiredSubscriptionsDate;
    private final HackathonState state;
    private final Soldi moneyPrize;
    private final int maxTeamDimension;
    @JsonIncludeProperties({"name","id", "grade"})
    private final Team winner;

    public Report(String name, String ruleBook, Prenotazione reservation, LocalDate expiredSubscriptionsDate, HackathonState state, Soldi moneyPrize, int maxTeamDimension, Team winner) {
        this.name = name;
        this.ruleBook = ruleBook;
        this.reservation = reservation;
        this.expiredSubscriptionsDate = expiredSubscriptionsDate;
        this.state = state;
        this.moneyPrize = moneyPrize;
        this.maxTeamDimension = maxTeamDimension;
        this.winner = winner;
    }
}
