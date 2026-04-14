package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
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

    public Report(String name, int maxTeamDimension, Soldi moneyPrize, HackathonState state, Prenotazione reservation, String ruleBook) {
        this.name = name;
        this.maxTeamDimension = maxTeamDimension;
        this.moneyPrize = moneyPrize;
        this.state = state;
        this.reservation = reservation;
        this.ruleBook = ruleBook;
    }
}
