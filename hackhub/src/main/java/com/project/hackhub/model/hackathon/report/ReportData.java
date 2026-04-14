package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
/**
 * Intermediate aggregated data model used to construct different
 * Report final classes.
 *
 * <p>This class collects all domain information required by the report
 * builders before transforming it into a specific representation
 * (e.g. public, detailed, or staff view).</p>
 *
 * <p>It is not exposed directly to clients and is intended solely as
 * an internal data carrier between the domain layer and report DTOs.</p>
 */
@Getter @Setter
public class ReportData {
    private String name;
    private String ruleBook;
    private LocalDate expiredSubscriptionsDate;
    private int maxTeamDimension;
    private HackathonState state;
    private List<Team> teamsList;
    private List<UtenteRegistrato> mentorsList;
    private Soldi moneyPrice;
    private UtenteRegistrato judge;
    private UtenteRegistrato coordinator;
    private Prenotazione reservation;
    private List<String> teamsGrades;
    private List<AidRequest> aidRequests;
    private List<Infraction> infractions;
    private Team winner;
}
