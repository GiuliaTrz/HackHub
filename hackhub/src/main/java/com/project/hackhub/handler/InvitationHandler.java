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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class InvitationHandler {

    private final UtenteRegistratoRepository userRepository;
    private final TeamRepository teamRepository;
    private final InvitoRepository invitoRepository;

    /**
     * Invites a {@link UtenteRegistrato} to take part in a {@link Team}.
     *
     * @param user the user to invite
     * @param team the team that extends the invite
     * @throws UserNotAvailableException if the user is not available
     * @throws IllegalArgumentException if the invitation or the team member do not exist in the
     * repositories or if the ids are {@code null}
     * @throws UnsupportedOperationException if the user that initiated the operation does not
     * have permission to cancel the invitation or if the {@link com.project.hackhub.model.hackathon.Hackathon}
     * is not {@link HackathonStateType#IN_ISCRIZIONE}
     * @author Giorgia Branchesi
     */
    @Transactional
    public void inviteUser(UUID teamLeader, UUID user, UUID team) {

        UtenteRegistrato teamLeader1 = userRepository.findById(teamLeader).orElseThrow(
                () -> new IllegalArgumentException("Team leader does not exist")
        );
        UtenteRegistrato userToInvite = userRepository.findById(user).orElseThrow(
                () -> new IllegalArgumentException("User to invite does not exist")
        );

        Team t = teamRepository.findById(team).orElseThrow(
                () -> new IllegalArgumentException("team does not exist")
        );

        if (!t.getHackathon().getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
                throw new IllegalStateException("Hackathon must be IN_ISCRIZIONE to invite users.");
        if(!teamLeader1.hasPermission(Permission.CAN_INVITE_USERS, t.getHackathon()))
            throw new UnsupportedOperationException("Action not allowed.");

       if (userToInvite.isAvailable(t.getHackathon().getReservation())) {
            Invito invitation = new Invito(t, userToInvite);
            t.addInvitation(invitation);
            teamRepository.save(t);
            EventManager notifier = EventManager.getInstance();
            notifier.notify(EventType.INVITO_UTENTE, List.of(userToInvite), invitation);
        } else {
            throw new UserNotAvailableException("User is not available and cannot be invited!");
        }
    }

    /**
     * Cancels an invitation that was sent and not yet accepted
     * @param invitation the invitation to cancel
     * @param teamMember the team member that wants to cancel the invitation
     * @throws IllegalArgumentException if the invitation or the team member do not exist in the
     * repositories or if the ids are {@code null}
     * @throws UnsupportedOperationException if the user that initiated the operation does not
     * have permission to cancel the invitation or if the {@link com.project.hackhub.model.hackathon.Hackathon}
     * is not {@link HackathonStateType#IN_ISCRIZIONE}
     */
    @Transactional
    public void cancelInvitation(UUID invitation, UUID teamMember) {

        Invito toCancel = invitoRepository.findById(invitation).orElseThrow(
                () -> new IllegalArgumentException("invitation to cancel does not exist"));

        Team t = toCancel.getSender();
        UtenteRegistrato tMember = userRepository.findById(teamMember).orElseThrow(() -> new IllegalArgumentException("user does not exist"));

        if(!t.getHackathon().getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE)
                || !tMember.hasPermission(Permission.CAN_CANCEL_INVITATION, t.getHackathon()))
            throw new UnsupportedOperationException("Action not allowed.");

        t.removeInvitationFromList(toCancel);
        teamRepository.save(t);
        toCancel.getAddresee().removeInvitation(toCancel);
        userRepository.save(toCancel.getAddresee());
        invitoRepository.delete(toCancel);
    }
}
