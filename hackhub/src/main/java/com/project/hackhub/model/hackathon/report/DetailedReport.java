package com.project.hackhub.model.hackathon.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
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
    private final UtenteRegistrato coordinator;

    @JsonIgnoreProperties({"passwordHash", "organizer"})
    private final UtenteRegistrato judge;

    @JsonIgnoreProperties({"passwordHash", "organizer"})
    private final List<UtenteRegistrato> mentors;

    public DetailedReport(String name, String ruleBook, Prenotazione reservation, LocalDate expiredSubscriptionsDate, HackathonState state, Soldi moneyPrize, int maxTeamDimension, Team winner, UtenteRegistrato coordinator, UtenteRegistrato judge, List<UtenteRegistrato> mentors) {
        super(name, ruleBook, reservation, expiredSubscriptionsDate, state, moneyPrize, maxTeamDimension, winner);
        this.coordinator = coordinator;
        this.judge = judge;
        this.mentors = mentors;
    }
}