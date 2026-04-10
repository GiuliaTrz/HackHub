package com.project.hackhub.handler;

import com.project.hackhub.exceptions.UserNotAvailableException;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.InvitoRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class InvitationHandler {

    private final UtenteRegistratoRepository utenteRegistratoRepo;
    private final TeamRepository teamRepository;
    private final InvitoRepository invitoRepository;

    /**
     * Invites a {@link UtenteRegistrato} to take part in a {@link Team}.
     *
     * @param user the user to invite
     * @param team the team that extends the invite
     * @throws UserNotAvailableException if the user is not available
     */
    public void inviteUser(UUID user, UUID team) {

        UtenteRegistrato userToInvite = utenteRegistratoRepo.findById(user).orElseThrow(
                () -> new IllegalArgumentException("user to invite does not exist")
        );

        Team t = teamRepository.findById(team).orElseThrow(
                () -> new IllegalArgumentException("team does not exist")
        );
        // Check if hackathon is open for registration and if team leader has permission
        if (!t.getHackathon().getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE)
                || !t.getTeamLeader().hasPermission(Permission.CAN_INVITE_USERS, t.getHackathon()))
            throw new UnsupportedOperationException("Action not allowed.");

        // Check if user is available
        if (userToInvite.isAvailable(t.getHackathon().getReservation())) {
            Invito invitation = new Invito(t, userToInvite);
            invitoRepository.save(invitation);
            t.addInvitation(invitation);
            teamRepository.save(t);

            EventManager notifier = EventManager.getInstance();
            notifier.notify(EventType.INVITO_UTENTE, List.of(userToInvite), invitation);
        } else {
            throw new UserNotAvailableException("User is not available and cannot be invited!");
        }
    }
}
