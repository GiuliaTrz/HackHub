package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonStateFactory;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.UtenteRegistratoRepository;
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

    /**
     * Resets the builder to create a new Hackathon instance.
     */
    @Override
    public void reset() {
            hackathon = new Hackathon();
    }

    /**
     * Sets the name of the hackathon if the provided name is not null or blank.
     * @param n the name to set
     */
    @Override
    public void setName(String n) {

        if(n != null && !n.isBlank())
             hackathon.setName(n);

    }

    /**
     * Sets the rule book of the hackathon if the provided rule book is not null or blank.
     * @param r the rule book to set
     */
    @Override
    public void setRuleBook(String r) {

        if(r != null && !r.isBlank())
            hackathon.setRuleBook(r);
    }

    /**
     * Sets the state of the hackathon to the default 'IN_ISCRIZIONE' state.
     */
    @Override
    public void setState() {
        hackathon.setStateType(HackathonStateType.IN_ISCRIZIONE);
    }

    /**
     * Sets the state type of the hackathon.
     * @param stateType the state type to set
     */
    @Override
    public void setStateType(HackathonStateType stateType) {
        hackathon.setStateType(stateType);
    }

    /**
     * Sets the maximum team dimension if the number is between 2 and 19.
     * @param num the maximum team dimension
     */
    @Override
    public void setMaxTeamDimension(Integer num) {

        if(num != null && num > 1 && num < 20)
            hackathon.setMaxTeamDimension(num);
    }

    /**
     * Sets the reservation for the hackathon.
     * @param p the reservation
     */
    @Override
    public void setReservation(Prenotazione p) {
        hackathon.setReservation(p);
    }

    /**
     * Sets the money price if the Soldi object is valid (not null and quantity >= 0).
     * @param p the money price
     */
    @Override
    public void setMoneyPrice(Soldi p) {
        if(p != null && p.getQuantity() >= 0)
            hackathon.setMoneyPrice(p);
    }

    /**
     * Adds the list of mentors to the hackathon.
     * @param mentorsList the list of mentors
     */
    @Override
    public void addMentorsList(List<UtenteRegistrato> mentorsList) {
        if(mentorsList!=null)
            hackathon.setMentorsList(mentorsList);
    }

    /**
     * Sets the expired subscription date if it is before now plus 31 days.
     * @param d the expired subscription date
     */
    @Override
    public void setExpiredSubscriptionDate(LocalDate d) {

        if(d != null && d.isBefore(LocalDate.now().plusDays(31)))
            hackathon.setExpiredSubscriptionsDate(d);
    }

    /**
     * Sets the judge for the hackathon.
     * @param u the judge
     */
    @Override
    public void setJudge(UtenteRegistrato u) {
            hackathon.setJudge(u);

    }

    /**
     * Sets the coordinator for the hackathon.
     * @param coordinator the coordinator
     */
    @Override
    public void setCoordinator(UtenteRegistrato coordinator) {
        hackathon.setCoordinator(coordinator);
    }

    /**
     * Returns the built Hackathon instance.
     * @return the Hackathon instance
     */
    public Hackathon getProduct() {
        return this.hackathon;
    }

    /**
     * Saves or updates the current builder state as a memento
     *
     * @param author the author of the memento
     * @return a new HackathonBuilderMemento or updates an existing one
     */
    public HackathonBuilderMemento saveMemento(UtenteRegistrato author) {
        return HackathonBuilderMemento.fromBuilder(this, author);
    }

    /**
     * Restores the builder state from the given memento.
     * @param memento the memento to restore from
     */
    public void restoreMemento(HackathonBuilderMemento memento) {
        memento.restoreInto(this);
    }

    /**
     * Restores the builder state from a snapshot, including associated users.
     * @param snapshot the snapshot to restore from
     * @param userRepository repository to resolve user UUIDs
     */
    public void restoreFromSnapshot(HackathonSnapshot snapshot, UtenteRegistratoRepository userRepository) {
        // Restore basic data
        HackathonBuilderMemento memento = new HackathonBuilderMemento(snapshot);
        restoreMemento(memento);

        // Restore associated users
        if (snapshot.getMentorsList() != null && !snapshot.getMentorsList().isEmpty()) {
            List<UtenteRegistrato> mentors = snapshot.getMentorsList().stream()
                    .map(uuid -> userRepository.findById(uuid)
                            .orElseThrow(() -> new IllegalArgumentException("Mentor not found: " + uuid)))
                    .toList();
            addMentorsList(mentors);
        }

        if (snapshot.getJudge() != null) {
            UtenteRegistrato judge = userRepository.findById(snapshot.getJudge())
                    .orElseThrow(() -> new IllegalArgumentException("Judge not found: " + snapshot.getJudge()));
            setJudge(judge);
        }
    }

    public boolean isComplete() {
        return hackathon.getName() != null
                && hackathon.getRuleBook() != null
                && hackathon.getExpiredSubscriptionsDate() != null
                && hackathon.getMaxTeamDimension() != null
                && hackathon.getMaxTeamDimension() != 0
                && hackathon.getMoneyPrice() != null
                && hackathon.getJudge() != null
                && hackathon.getReservation() != null
                && hackathon.getMentorsList() != null;
    }
}
