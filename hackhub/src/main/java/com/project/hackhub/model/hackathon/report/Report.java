package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Interface for all Hackathon report representations.
 *
 * <p>Implemented by different DTOs representing the same underlying data
 * exposed with different visibility levels depending on user permissions.</p>
 */
@Getter
public abstract class Report {

    private final String name;
    private final String ruleBook;
    private final Prenotazione reservation;
    private final HackathonState state;
    private final Soldi moneyPrize;
    private final int maxTeamDimension;
    private final Team winner;

    public Report(String name, String ruleBook, Prenotazione reservation, HackathonState state, Soldi moneyPrize, int maxTeamDimension, Team winner) {
        this.name = name;
        this.ruleBook = ruleBook;
        this.reservation = reservation;
        this.state = state;
        this.moneyPrize = moneyPrize;
        this.maxTeamDimension = maxTeamDimension;
        this.winner = winner;
    }
}
