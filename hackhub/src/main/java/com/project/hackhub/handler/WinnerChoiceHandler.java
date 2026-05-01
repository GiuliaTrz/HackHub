package com.project.hackhub.handler;

import com.project.hackhub.exceptions.MultipleWinnersException;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.project.hackhub.observer.EventType.PROCLAIM_WINNER;
import static com.project.hackhub.observer.EventType.WINNER_CHOICE;

@Service
public class WinnerChoiceHandler {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public WinnerChoiceHandler(HackathonRepository hackathonRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.hackathonRepository = hackathonRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    private List<Team> getTeamsWithMaxGrade(UUID hackathon){
        return this.teamRepository.getTeamsWithMaxGrade(hackathon);
    }

    /**
     * Checks the judge having valid credentials and permission for the operation, as well as that
     * the Hackathon is in the state APPRAISAL
     * @param judge the id of the user
     * @param hackathon the id of the Hackathon of interest
     * @throws IllegalArgumentException if no corresponding registered users or hackathon are found,
     * or if the user does not have the required permission
     * @return the Hackathon corresponding to hackathon id given
     * @author Chiara Marinucci
     */
    private Hackathon checkValid(UUID judge, UUID hackathon) {
        User j = this.userRepository.findById(judge)
                .orElseThrow(() -> new IllegalArgumentException("judge not found"));
        Hackathon h = this.hackathonRepository.findById(hackathon)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        if(h.getState().getStateType()!= HackathonStateType.APPRAISAL)
             throw new IllegalStateException("Hackathon must be in state APPRAISAL");
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
    @Transactional
    public void chooseWinner(UUID judge, UUID hackathon){
        Hackathon h = checkValid(judge, hackathon);

        List<Team> winners = getTeamsWithMaxGrade(hackathon);
        if(winners.size() == 1) {
            h.setWinner(winners.getFirst());
            this.hackathonRepository.save(h);
            List<User> toBeNotified = new ArrayList<>();
            toBeNotified.add(h.getCoordinator());
            EventManager.getInstance().notify(WINNER_CHOICE,toBeNotified , h);
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
     * @param team the winner that has been chosen
     * @throws IllegalArgumentException if the given team is not registered to the Hackathon or
     * if the team is not among the tied winners list.
     * @author Chiara Marinucci
     */
    @Transactional
    public void chooseWinner(UUID judge, UUID hackathon, UUID team){
        Hackathon h = checkValid(judge, hackathon);
        Team t = this.teamRepository.findById(team).orElseThrow(() -> new IllegalArgumentException("team not found"));
        if(!t.getHackathon().getId().equals(hackathon))
            throw new IllegalArgumentException("The given team is not registered to the Hackathon");
        List<Team> tiedWinners = getTeamsWithMaxGrade(hackathon);
        boolean isContained = tiedWinners.stream()
                .anyMatch(t1 -> t1.getId().equals(team));
        if(!isContained)
            throw new IllegalArgumentException(("The winner team can't be chosen" +
                    " outside of the teams with highest grade for the Hackathon"));
        h.setWinner(t);
        this.hackathonRepository.save(h);
        List<User> toBeNotified = new ArrayList<>();
        toBeNotified.add(h.getCoordinator());
        EventManager.getInstance().notify(WINNER_CHOICE,toBeNotified , h);
    }

    /**
     * Allows a coordinator to officially declare, if present, a winner. This action will change
     * the Hackathon's state to CONCLUDED.
     * @param hackathon the id of the Hackathon of interest
     * @param coord the id of the user
     * @throws IllegalArgumentException if the coordinator or the Hackathon are not found, or if the coordinator
     * lacks required permission for the action.
     * @throws IllegalStateException if the Hackathon's state is not APPRAISAL
     * @author Chiara Marinucci
     */
    @Transactional
    public void proclaimWinner(UUID hackathon, UUID coord){
        User c = this.userRepository.findById(coord)
                .orElseThrow(() -> new IllegalArgumentException("coordinator not found"));
        Hackathon h = this.hackathonRepository.findById(hackathon)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        if(h.getState().getStateType()!= HackathonStateType.APPRAISAL)
             throw new IllegalStateException("Hackathon must be in state APPRAISAL");
        if(!c.hasPermission(Permission.CAN_PROCLAIM_WINNER, h))
             throw new IllegalArgumentException("user does not have required permission");

        if(h.getWinner()!= null){
             List<User> toBeNotified = new ArrayList<>();
             for(Team t : h.getTeamsList()){
             toBeNotified.addAll(t.getTeamMembersList());
             }
             EventManager.getInstance().notify(PROCLAIM_WINNER,toBeNotified , h);
         }
         setConcluded(h);
     }

    /**
     * Helper method that sets the Hackathon state to CONCLUDED and saves changes.
     * @param h the Hackathon of interest
     */
    private void setConcluded(Hackathon h) {
        h.setStateType(HackathonStateType.CONCLUDED);
        this.hackathonRepository.save(h);
    }


}
