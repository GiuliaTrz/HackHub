package com.project.hackhub.handler;

import com.project.hackhub.exceptions.UserNotAvailableException;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invitation;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.InvitationRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class InvitationHandler {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final InvitationRepository invitationRepository;

    /**
     * Invites a {@link User} to take part in a {@link Team}.
     *
     * @param user the user to invite
     * @param team the team that extends the invite
     * @throws UserNotAvailableException if the user is not available
     * @throws IllegalArgumentException if the invitation or the team member do not exist in the
     * repositories or if the ids are {@code null}
     * @throws UnsupportedOperationException if the user that initiated the operation does not
     * have permission to cancel the invitation or if the {@link com.project.hackhub.model.hackathon.Hackathon}
     * is not {@link HackathonStateType#SUBSCRIPTION_PHASE}
     * @author Giorgia Branchesi
     */
    @Transactional
    public void inviteUser(UUID teamLeader, UUID user, UUID team) {

        User teamLeader1 = userRepository.findById(teamLeader).orElseThrow(
                () -> new IllegalArgumentException("Team leader does not exist")
        );
        User userToInvite = userRepository.findById(user).orElseThrow(
                () -> new IllegalArgumentException("User to invite does not exist")
        );

        Team t = teamRepository.findById(team).orElseThrow(
                () -> new IllegalArgumentException("team does not exist")
        );

        if (!t.getHackathon().getState().getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE))
                throw new IllegalStateException("Hackathon must be in SUBSCRIPTION_PHASE to invite users.");
        if(!teamLeader1.hasPermission(Permission.CAN_INVITE_USERS, t.getHackathon()))
            throw new UnsupportedOperationException("Action not allowed.");
        if(!teamLeader1.equals(t.getTeamLeader()))
            throw new IllegalArgumentException("Cannot invite users to other people's team!");

        if(invitationRepository.existsBySenderAndAddresseeAndPendingTrue(t, userToInvite))
            throw new IllegalArgumentException("An invitation has already been sent to this user for this team!");

       if (userToInvite.isAvailable(t.getHackathon().getReservation())) {
            Invitation invitation = new Invitation(t, userToInvite);
            t.addInvitation(invitation);
            teamRepository.save(t);
            EventManager notifier = EventManager.getInstance();
            notifier.notify(EventType.USER_INVITATION, List.of(userToInvite), "you have been invited on a team!", invitation);
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
     * is not {@link HackathonStateType#SUBSCRIPTION_PHASE}
     */
    @Transactional
    public void cancelInvitation(UUID invitation, UUID teamMember) {

        Invitation toCancel = invitationRepository.findById(invitation).orElseThrow(
                () -> new IllegalArgumentException("invitation to cancel does not exist"));

        Team t = toCancel.getSender();
        User tMember = userRepository.findById(teamMember).orElseThrow(() -> new IllegalArgumentException("user does not exist"));

        if(!t.getHackathon().getState().getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE)
                || !tMember.hasPermission(Permission.CAN_CANCEL_INVITATION, t.getHackathon()))
            throw new UnsupportedOperationException("Action not allowed.");
        if(!(tMember.equals(t.getTeamLeader())) && !(tMember.equals(toCancel.getAddressee())))
            throw new IllegalArgumentException("Only the team leader or the addressee can cancel the invitation!");

        t.removeInvitationFromList(toCancel);
        teamRepository.save(t);
        toCancel.getAddressee().removeInvitation(toCancel);
        userRepository.save(toCancel.getAddressee());
        invitationRepository.delete(toCancel);
    }
}
