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

import java.util.UUID;

@Component
@AllArgsConstructor
public class PartecipationHandler {

    private final TeamRepository teamRepository;
    private final UtenteRegistratoRepository userRepository;

    /**
     * Unsubscribes a team from a Hackathon. The team does not exist after his unsubscription
     * @param team the team
     * @throws IllegalArgumentException if any of the parameters are null or do not exist
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#IN_ISCRIZIONE} state
     * or the user does not have permission to do the action
     * @author Giorgia Branchesi
     */
    @Transactional
    public void unsubscribeTeam(UUID team, UUID user){

        Team t = teamRepository.findById(team).orElseThrow(() ->
           new IllegalArgumentException("team cannot be null"));

        UtenteRegistrato user1 = userRepository.findById(user).orElseThrow(
                () -> new IllegalArgumentException("user cannot be null"));

        Hackathon h = t.getHackathon();

        if(!user1.hasPermission(Permission.CAN_UNSUBSCRIBE_TEAM, h)
                || !h.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
            throw new UnsupportedOperationException("Cannot unsubscribe team");

        h.removeTeam(t);
        EventManager notifier = EventManager.getInstance();
        notifier.notify(EventType.ELIMINAZIONE_TEAM, t.getTeamMembersList(), t);
        teamRepository.delete(t);
    }
}
