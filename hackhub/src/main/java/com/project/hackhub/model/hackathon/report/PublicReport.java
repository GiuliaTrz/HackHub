package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import lombok.Getter;

import java.util.List;

/**
 * Publicly visible report view with restricted information.
 */
@Getter
public final class PublicReport extends Report {

    private final List<String> teamsGrades;

    public PublicReport(String name, String ruleBook, Prenotazione reservation, HackathonState state, Soldi moneyPrize, int maxTeamDimension, Team winner, List<String> teamsGrades) {
        super(name, ruleBook, reservation, state, moneyPrize, maxTeamDimension, winner);
        this.teamsGrades = teamsGrades;
    }
}
