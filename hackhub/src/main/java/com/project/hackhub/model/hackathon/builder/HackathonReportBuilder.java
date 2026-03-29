package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Report;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.time.LocalDate;
import java.util.List;

public class HackathonReportBuilder implements Builder {

    private Report report;

    @Override
    public void reset() {

    }

    @Override
    public void setName(String n) {

    }

    @Override
    public void setRuleBook(String r) {

    }

    @Override
    public void setState() {

    }

    @Override
    public void setMaxTeamDimension(int num) {

    }

    @Override
    public void setReservation(Prenotazione p) {

    }

    @Override
    public void setMoneyPrice(Soldi p) {

    }

    @Override
    public void addMentorsList(List<UtenteRegistrato> mentorsList) {

    }

    @Override
    public void setExpiredSubscriptionDate(LocalDate d) {

    }

    @Override
    public void setJudge(UtenteRegistrato u) {

    }

    @Override
    public void setCoordinator(UtenteRegistrato coordinator) {

    }
}
