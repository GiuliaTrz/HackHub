package com.project.hackhub.service;

import com.project.hackhub.exceptions.UserNotAvailableException;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;

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

        if(user.isAvailable(team.getHackathon().getReservation()))
        {
            Invito invite = new Invito(team, user);
            team.addInvitation(invite);
            teamRepository.save(team);
            EventManager notifier = new EventManager();
            notifier.notify();
        }
        else throw new UserNotAvailableException("L'utente non è disponibile, non può essere invitato!");
    }
}
