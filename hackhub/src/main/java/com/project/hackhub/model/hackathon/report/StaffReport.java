package com.project.hackhub.model.hackathon.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
import java.util.Map;

/**
 * Full report view intended for staff members.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class StaffReport extends Report {

    private final List<Team> teams;

    @JsonIgnoreProperties({"passwordHash", "organizer"})
    private final List<UtenteRegistrato> mentors;

    @JsonIgnoreProperties({"passwordHash", "organizer"})
    private final UtenteRegistrato coordinator;

    @JsonIgnoreProperties({"passwordHash", "organizer"})
    private final UtenteRegistrato judge;

    private final Map<String, Float> teamsGrades;
    private final List<AidRequest> aidRequests;
    private final List<Infraction> infractions;

    public StaffReport(String name, String ruleBook, Prenotazione reservation, HackathonState state, Soldi moneyPrize, int maxTeamDimension, Team winner, List<Team> teams, List<UtenteRegistrato> mentors, UtenteRegistrato coordinator, UtenteRegistrato judge, Map<String, Float> teamsGrades, List<AidRequest> aidRequests, LocalDate expiredSubscriptionsDate, List<Infraction> infractions) {
        super(name, ruleBook, reservation, expiredSubscriptionsDate, state, moneyPrize, maxTeamDimension, winner);
        this.teams = teams;
        this.mentors = mentors;
        this.coordinator = coordinator;
        this.judge = judge;
        this.teamsGrades = teamsGrades;
        this.aidRequests = aidRequests;
        this.infractions = infractions;
    }
}
