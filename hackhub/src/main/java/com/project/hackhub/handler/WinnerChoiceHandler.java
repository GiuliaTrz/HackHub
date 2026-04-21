package com.project.hackhub.handler;

import com.project.hackhub.exceptions.MultipleWinnersException;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.project.hackhub.observer.EventType.PENALIZZAZIONE_TEAM;
import static com.project.hackhub.observer.EventType.SCELTA_VINCITORE;

public class WinnerChoiceHandler {

    private final HackathonRepository hackathonRepository;
    private final UtenteRegistratoRepository utenteRegistratoRepository;
    private final TeamRepository teamRepository;

    public WinnerChoiceHandler(HackathonRepository hackathonRepository, UtenteRegistratoRepository utenteRegistratoRepository, TeamRepository teamRepository) {
        this.hackathonRepository = hackathonRepository;
        this.utenteRegistratoRepository = utenteRegistratoRepository;
        this.teamRepository = teamRepository;
    }

    private List<Team> getTeamsWithMaxGrade(UUID hackathon){
        return this.teamRepository.getTeamsWithMaxGrade(hackathon);
    }

    /**
     * Checks the judge having valid credentials and permission for the operation, as well as that
     * the Hackathon is in the state IN_VALUTAZIONE
     * @param judge the id of the user
     * @param hackathon the id of the Hackathon of interest
     * @throws IllegalArgumentException if no corresponding registered users or hackathon are found,
     * or if the user does not have the required permission
     * @return the Hackathon corresponding to hackathon id given
     * @author Chiara Marinucci
     */
    private Hackathon checkValid(UUID judge, UUID hackathon) {
        UtenteRegistrato j = this.utenteRegistratoRepository.findById(judge)
                .orElseThrow(() -> new IllegalArgumentException("judge not found"));
        Hackathon h = this.hackathonRepository.findById(hackathon)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        if(h.getState().getStateType()!= HackathonStateType.IN_VALUTAZIONE)
            throw new IllegalStateException("Hackathon must be in state IN_VALUTAZIONE");
        if(!j.hasPermission(Permission.CAN_CHOOSE_WINNER, h))
            throw new IllegalArgumentException("user does not have required permission");
        return h;
    }

    /**
     * Allows a judge to choose the winner team for a hackathon.
     * This means setting the winner field in Hackathon with the highest grade. If more teams are tied
     * a MultipleWinnerException is thrown to proceed to choose manually from the tied winners' list.
     * @param judge id of the user
     * @param hackathon id of the hackathon of interest
     * @throws IllegalArgumentException if there are no winners, e.g. no team sent a submission
     * @throws MultipleWinnersException if there are more teams that are tied and the judge must proceed
     * with manual choice
     * @author Chiara Marinucci
     */
    public void chooseWinner(UUID judge, UUID hackathon){
        Hackathon h = checkValid(judge, hackathon);

        List<Team> winners = getTeamsWithMaxGrade(hackathon);
        if(winners.size() == 1) {
            h.setWinner(winners.getFirst());
            this.hackathonRepository.save(h);
            List<UtenteRegistrato> toBeNotified = new ArrayList<>();
            toBeNotified.add(h.getCoordinator());
            EventManager.getInstance().notify(SCELTA_VINCITORE,toBeNotified , h);
        }
        else if (winners.isEmpty())
            throw new IllegalArgumentException("There are no winners");
        else
            throw new MultipleWinnersException(winners);
    }


    /**
     * A method that allows a judge to choose manually a winner team in a situation where more teams are tied.
     * @param judge the id of the user
     * @param hackathon the id of the Hackathon of interest
     * @param team the winner that has been choosen
     * @throws IllegalArgumentException if the given team is not registered to the Hackathon or
     * if the team is not among the tied winners list.
     * @author Chiara Marinucci
     */
    public void chooseWinner(UUID judge, UUID hackathon, Team team){
        Hackathon h = checkValid(judge, hackathon);
        if(!team.getHackathon().getId().equals(hackathon))
            throw new IllegalArgumentException("The given team is not registered to the Hackathon");
        List<Team> tiedWinners = getTeamsWithMaxGrade(hackathon);
        boolean isContained = tiedWinners.stream()
                .anyMatch(t -> t.getId().equals(team.getId()));
        if(!isContained)
            throw new IllegalArgumentException(("The winner team can't be chosen" +
                    " outside of the teams with highest grade for the Hackathon"));
        h.setWinner(team);
        this.hackathonRepository.save(h);
        List<UtenteRegistrato> toBeNotified = new ArrayList<>();
        toBeNotified.add(h.getCoordinator());
        EventManager.getInstance().notify(SCELTA_VINCITORE,toBeNotified , h);
    }
}
