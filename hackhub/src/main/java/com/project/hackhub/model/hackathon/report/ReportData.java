package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import java.time.LocalDate;
import java.util.List;
/**
 * Intermediate aggregated data model used to construct different
 * Report DTOs.
 *
 * <p>This class collects all domain information required by the report
 * builders before transforming it into a specific representation
 * (e.g. public, detailed, or staff view).</p>
 *
 * <p>It is not exposed directly to clients and is intended solely as
 * an internal data carrier between the domain layer and report DTOs.</p>
 */
public class ReportData {
    private String name;
    private String description;
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
    private String teamsGrades;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRuleBook() {
        return ruleBook;
    }

    public void setRuleBook(String ruleBook) {
        this.ruleBook = ruleBook;
    }

    public LocalDate getExpiredSubscriptionsDate() {
        return expiredSubscriptionsDate;
    }

    public void setExpiredSubscriptionsDate(LocalDate expiredSubscriptionsDate) {
        this.expiredSubscriptionsDate = expiredSubscriptionsDate;
    }

    public int getMaxTeamDimension() {
        return maxTeamDimension;
    }

    public void setMaxTeamDimension(int maxTeamDimension) {
        this.maxTeamDimension = maxTeamDimension;
    }

    public HackathonState getState() {
        return state;
    }

    public void setState(HackathonState state) {
        this.state = state;
    }

    public List<Team> getTeamsList() {
        return teamsList;
    }

    public void setTeamsList(List<Team> teamsList) {
        this.teamsList = teamsList;
    }

    public List<UtenteRegistrato> getMentorsList() {
        return mentorsList;
    }

    public void setMentorsList(List<UtenteRegistrato> mentorsList) {
        this.mentorsList = mentorsList;
    }

    public Soldi getMoneyPrice() {
        return moneyPrice;
    }

    public void setMoneyPrice(Soldi moneyPrice) {
        this.moneyPrice = moneyPrice;
    }

    public UtenteRegistrato getJudge() {
        return judge;
    }

    public void setJudge(UtenteRegistrato judge) {
        this.judge = judge;
    }

    public UtenteRegistrato getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(UtenteRegistrato coordinator) {
        this.coordinator = coordinator;
    }

    public Prenotazione getReservation() {
        return reservation;
    }

    public void setReservation(Prenotazione reservation) {
        this.reservation = reservation;
    }

    public String getTeamsGrades() {
        return teamsGrades;
    }

    public void setTeamsGrades(String string) {
        this.teamsGrades = teamsGrades;
    }
}
