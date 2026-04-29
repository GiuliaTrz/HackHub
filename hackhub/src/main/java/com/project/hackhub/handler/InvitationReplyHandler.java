package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invitation;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.model.user.state.UserStateType;
import com.project.hackhub.repository.InvitationRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UserRepository;
import com.project.hackhub.service.UserStateService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class InvitationReplyHandler {

    private final InvitationRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserStateService userStateService;

    /**
     * Deletes the invitation for a user
     *
     * @param invitation the invitation
     * @throws IllegalArgumentException if the invitation is null
     * @author Giorgia Branchesi
     */
    private void removeInvitation(UUID invitation) {

        Invitation i = invitationRepository.findById(invitation).orElseThrow(
                () -> new IllegalArgumentException("the invitation cannot be null"));

        Team t = i.getSender();
        t.removeInvitationFromList(i);
        teamRepository.save(t);
        invitationRepository.delete(i);
    }

    /**
     * Accept invitation to take part on a team
     *
     * @param invitation the UUID of the invitation to accept
     * @throws IllegalArgumentException      if the invitation is {@code null}
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#SUBSCRIPTION_PHASE}
     *                                       or if the user is not available, so they can't accept the invitation
     * @author Giorgia Branchesi
     */
    @Transactional
    public void acceptInvitation(UUID user, UUID invitation) {

        User u = userRepository.findById(user).orElseThrow(
                () -> new IllegalArgumentException("user cannot be null"));
        Invitation i = invitationRepository.findById(invitation).orElseThrow(
                () -> new IllegalArgumentException("the invitation cannot be null"));

        User addressee = i.getAddressee();
        Hackathon h = i.getSender().getHackathon();

        if(!u.hasPermission(Permission.CAN_ACCEPT_INVITATION, h)) {
           throw new IllegalArgumentException("user does not have permission "); }

        if(!h.getState().getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE)
                || !addressee.isAvailable(h.getReservation()))
            throw new UnsupportedOperationException("cannot perform operation");

        addressee.removeInvitation(i);
        userStateService.changeUserState(addressee, true, h, UserStateType.TEAM_MEMBER);
        i.getSender().addTeamMember(addressee);
        removeInvitation(invitation);
    }

    /**
     * Declines invitation to take part on a team
     *
     * @param invitation the UUID of the invitation to decline
     * @throws IllegalArgumentException      if the invitation is {@code null}
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#SUBSCRIPTION_PHASE}
     * @author Giorgia Branchesi
     */
    @Transactional
    public void declineInvitation(UUID user, UUID invitation) {

        User u = userRepository.findById(user).orElseThrow(
                () -> new IllegalArgumentException("user cannot be null"));
        Invitation i = invitationRepository.findById(invitation).orElseThrow(
                () -> new IllegalArgumentException("the invitation cannot be null"));

        User addressee = i.getAddressee();
        Hackathon h = i.getSender().getHackathon();

        if (!u.hasPermission(Permission.CAN_DECLINE_INVITATION, h) || !h.getState().getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE))
            throw new UnsupportedOperationException("cannot perform operation");

        addressee.removeInvitation(i);
        userRepository.save(addressee);
        removeInvitation(invitation);
    }

}
