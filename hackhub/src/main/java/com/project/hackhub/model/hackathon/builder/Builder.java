package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.time.LocalDate;

public interface Builder {

    void reset();
    void setName(String n);
    void setRuleBook(String r);
    void setState(HackathonState state);
    void setMaxTeamDimension(int num);
    void setReservation(Prenotazione p);
    void setMoneyPrice(Soldi p);
    void addMentor(UtenteRegistrato utente);
    void setExpiredSubscriptionDate(LocalDate d);
    void createListTeams();
    void setJudge(UtenteRegistrato u);
}
