package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * Full report view intended for staff members.
 *
 */
@Getter
public final class StaffReport extends Report {

    private final List<Team> teams;
    private final List<UtenteRegistrato> mentors;
    private final UtenteRegistrato coordinator;
    private final UtenteRegistrato judge;
    private final List<String> teamsGrades;
    private final List<AidRequest> aidRequests;
    private final LocalDate expiredSubsciprionsDate;
    private final List<Infraction> infractions;

    public StaffReport(String name, String ruleBook, Prenotazione reservation, HackathonState state, Soldi moneyPrize, int maxTeamDimension, Team winner, List<Team> teams, List<UtenteRegistrato> mentors, UtenteRegistrato coordinator, UtenteRegistrato judge, List<String> teamsGrades, List<AidRequest> aidRequests, LocalDate expiredSubsciprionsDate, List<Infraction> infractions) {
        super(name, ruleBook, reservation, state, moneyPrize, maxTeamDimension, winner);
        this.teams = teams;
        this.mentors = mentors;
        this.coordinator = coordinator;
        this.judge = judge;
        this.teamsGrades = teamsGrades;
        this.aidRequests = aidRequests;
        this.expiredSubsciprionsDate = expiredSubsciprionsDate;
        this.infractions = infractions;
    }
}
