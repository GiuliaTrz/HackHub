package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonStateFactory;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class HackathonBuilder implements Builder {

    @Setter
    private Hackathon hackathon;

    @Override
    public void reset() {
            hackathon = new Hackathon();
    }

    @Override
    public void setName(String n) {
        hackathon.setName(n);

    }

    @Override
    public void setRuleBook(String r) {
        hackathon.setRuleBook(r);
    }

    @Override
    public void setState() {
        HackathonStateFactory factory = new HackathonStateFactory();
        HackathonState state = factory.createState(HackathonStateType.IN_ISCRIZIONE);
        hackathon.setState(state);
    }

    @Override
    public void setMaxTeamDimension(int num) {
        hackathon.setMaxTeamDimension(num);
    }

    @Override
    public void setReservation(Prenotazione p) {
        hackathon.setReservation(p);
    }

    @Override
    public void setMoneyPrice(Soldi p) {
            hackathon.setMoneyPrice(p);
    }

    @Override
    public void addMentorsList(List<UtenteRegistrato> mentorsList) {
            hackathon.setMentorsList(mentorsList);
    }

    @Override
    public void setExpiredSubscriptionDate(LocalDate d) {
            hackathon.setExpiredSubscriptionsDate(d);
    }

    @Override
    public void setJudge(UtenteRegistrato u) {
        hackathon.setJudge(u);

    }

    public Hackathon getProduct() {
        return this.hackathon;
    }

    public HackathonBuilderMemento saveMemento() {
        Hackathon memento = new Hackathon(this.hackathon);
        return new HackathonBuilderMemento((memento));
    }

    public void restoreMemento(HackathonBuilderMemento hackathonBuilderMemento){
        this.hackathon = hackathonBuilderMemento.getState();
    }
}
