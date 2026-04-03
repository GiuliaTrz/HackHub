package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Report;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.utente.UtenteRegistrato;
import java.time.LocalDate;
import java.util.List;

public class HackathonReportBuilder implements Builder {

    private Report report;

    public HackathonReportBuilder(){
        this.reset();;
    }

    @Override
    public void reset() {
        this.report = new Report();
    }

    @Override
    public void setName(String n) {
        this.report.setName(n);
    }

    @Override
    public void setRuleBook(String r) {
        this.report.setRuleBook(r);
    }

    @Override
    public void setState() {
        this.report.setState();
    }

    @Override
    public void setMaxTeamDimension(int num) {
        this.report.setMaxTeamDimension(num);
    }

    @Override
    public void setReservation(Prenotazione p) {
        this.report.setReservation(p);
    }

    @Override
    public void setMoneyPrice(Soldi p) {
        this.report.setMoneyPrice(p);
    }

    @Override
    public void addMentorsList(List<UtenteRegistrato> mentorsList) {
        this.report.setMentorsList(mentorsList);
    }

    @Override
    public void setExpiredSubscriptionDate(LocalDate d) {
        this.report.setExpiredSubscriptionsDate(d);
    }

    @Override
    public void setJudge(UtenteRegistrato u) {
        this.report.setJudge(u);
    }

    @Override
    public void setCoordinator(UtenteRegistrato coordinator) {
        this.report.setCoordinator(coordinator);
    }

    public Report build() {
        Report result = this.report;
        this.reset();
        return result;
    }
}
