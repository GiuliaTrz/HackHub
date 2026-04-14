package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.Getter;

import java.util.List;

/**
 *Detailed report view intended for users that have TEAM permission.
 */
@Getter
public final class DetailedReport extends Report {

    private final UtenteRegistrato coordinator;
    private final UtenteRegistrato judge;
    private final List<UtenteRegistrato> mentors;

    public DetailedReport(String name, int maxTeamDimension, Soldi moneyPrize, HackathonState state, Prenotazione reservation, String ruleBook, UtenteRegistrato coordinator, UtenteRegistrato judge, List<UtenteRegistrato> mentors) {
        super(name, maxTeamDimension, moneyPrize, state, reservation, ruleBook);
        this.coordinator = coordinator;
        this.judge = judge;
        this.mentors = mentors;
    }
}