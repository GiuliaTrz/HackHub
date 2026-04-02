package com.project.hackhub.service;

import com.project.hackhub.exceptions.UserNotAvailableException;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;

import java.util.List;

public class TeamHandler {

    private final UtenteRegistratoRepository userRepository;
    private final TeamRepository teamRepository;

    public TeamHandler(UtenteRegistratoRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Invites a {@link UtenteRegistrato} to take part in a {@link Team}.
     *
     * @param user the user to invite
     * @param team the team that extends the invite
     * @throws UserNotAvailableException if the user is not available
     *
     * @author Giorgia Branchesi
     */
    public void inviteUser(UtenteRegistrato user, Team team) {

        if(!team.getHackathon().getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE)
            || team.getTeamLeader().hasPermission(Permission.CAN_INVITE_USERS, team.getHackathon()))
                throw new UnsupportedOperationException("Azione non permessa.");

        if(user.isAvailable(team.getHackathon().getReservation()))
        {
            Invito invitation = new Invito(team, user);
            team.addInvitation(invitation);
            teamRepository.save(team);
            EventManager notifier = EventManager.getInstance();
            notifier.notify(EventType.INVITO_UTENTE, List.of(user), invitation);
        }
        else throw new UserNotAvailableException("L'utente non è disponibile, non può essere invitato!");
    }
}
