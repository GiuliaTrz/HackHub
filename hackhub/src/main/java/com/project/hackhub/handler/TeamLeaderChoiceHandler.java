package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class TeamLeaderChoiceHandler {

    private final TeamRepository teamRepository;
    private final UtenteRegistratoRepository userRepository;

    /**
     * Changes the leader of a team
     * @param newLeader the new leader
     * @param t the {@link Team}
     * @throws IllegalArgumentException if any of the parameters are null
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#IN_ISCRIZIONE} state
     * or the user does not have permission to do the action
     * @author Giorgia Branchesi
     */
    @Transactional
    public void chooseNewTeamLeader(UUID newLeader, UUID oldLeader, UUID t){

        UtenteRegistrato oldLeaderU = userRepository.findById(oldLeader).orElseThrow
                (() ->  new IllegalArgumentException("oldLeader cannot be null"));

        UtenteRegistrato newLeaderU = userRepository.findById(newLeader).orElseThrow
                (() ->  new IllegalArgumentException("newLeader cannot be null"));

        Team team = teamRepository.findById(t).orElseThrow
                (() ->  new IllegalArgumentException("team cannot be null"));

        if(!team.getTeamMembersList().contains(newLeaderU))
            throw new IllegalArgumentException("the new leader must be a member of the team");

        if(team.getTeamMembersList().size() == 1)
            throw new UnsupportedOperationException("Cannot choose new Leader with only one member");

        if(!oldLeaderU.hasPermission(Permission.CAN_MODIFY_LEADER, team.getHackathon()))
            throw new IllegalArgumentException("user does not have the permission to do this action");

        if(!team.getHackathon().getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
            throw new UnsupportedOperationException("Cannot choose new Leader");

        team.setTeamLeader(newLeaderU);
        teamRepository.save(team);
        EventManager notifier = EventManager.getInstance();
        notifier.notify(EventType.NUOVO_LEADER, List.of(oldLeaderU, newLeaderU), team);
    }
}
