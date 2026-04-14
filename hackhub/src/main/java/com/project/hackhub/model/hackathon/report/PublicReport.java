package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import lombok.Getter;

import java.util.List;

/**
 * Publicly visible report view with restricted information.
 */
@Getter
public final class PublicReport extends Report {

    private List<String> teamsGrades;

    public PublicReport(String name, int maxTeamDimension, Soldi moneyPrize, HackathonState state, Prenotazione reservation, String ruleBook, List<String> grades) {
        super(name, maxTeamDimension, moneyPrize, state, reservation, ruleBook);
        this.teamsGrades = grades;
    }

}
