package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.utente.UtenteRegistrato;
import java.time.LocalDate;
import java.util.List;

public interface Builder {

    void reset();
    void setName(String n);
    void setRuleBook(String r);
    void setState();
    void setMaxTeamDimension(Integer num);
    void setReservation(Prenotazione p);
    void setMoneyPrice(Soldi p);
    void addMentorsList(List<UtenteRegistrato> mentorsList);
    void setExpiredSubscriptionDate(LocalDate d);
    void setJudge(UtenteRegistrato u);
    void setCoordinator(UtenteRegistrato coordinator);
}
