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

/**
 * Concrete {@link Builder} class used to create {@link Hackathon} istances.
 * @author Giorgia Branchesi
 */
public class HackathonBuilder implements Builder {

    @Setter
    private Hackathon hackathon;

    @Override
    public void reset() {
            hackathon = new Hackathon();
    }

    @Override
    public void setName(String n) {

        if(n != null && !n.isBlank())
             hackathon.setName(n);

    }

    @Override
    public void setRuleBook(String r) {

        if(r != null && !r.isBlank())
            hackathon.setRuleBook(r);
    }

    @Override
    public void setState() {
        HackathonStateFactory factory = new HackathonStateFactory();
        HackathonState state = factory.createState(HackathonStateType.IN_ISCRIZIONE);
        hackathon.setState(state);
    }

    @Override
    public void setState(HackathonState state) {
        hackathon.setState(state);
    }

    @Override
    public void setMaxTeamDimension(int num) {

        if(num > 1 && num < 20)
            hackathon.setMaxTeamDimension(num);
    }

    @Override
    public void setReservation(Prenotazione p) {
        hackathon.setReservation(p);
    }

    @Override
    public void setMoneyPrice(Soldi p) {
         if(p != null && p.getQuantity() > 0)
            hackathon.setMoneyPrice(p);
    }

    @Override
    public void addMentorsList(List<UtenteRegistrato> mentorsList) {
        if(mentorsList!=null)
            hackathon.setMentorsList(mentorsList);
    }

    @Override
    public void setExpiredSubscriptionDate(LocalDate d) {

        if(d != null && d.isBefore(LocalDate.now().plusDays(31)))
            hackathon.setExpiredSubscriptionsDate(d);
    }

    @Override
    public void setJudge(UtenteRegistrato u) {
            hackathon.setJudge(u);

    }

    @Override
    public void setCoordinator(UtenteRegistrato coordinator) {
        hackathon.setCoordinator(coordinator);
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

    public boolean isComplete() {
        return hackathon.getName() != null
                && hackathon.getRuleBook() != null
                && hackathon.getExpiredSubscriptionsDate() != null
                && hackathon.getMaxTeamDimension() != 0
                && hackathon.getMoneyPrice() != null
                && hackathon.getJudge() != null
                && hackathon.getReservation() != null
                && hackathon.getMentorsList() != null;
    }
}
