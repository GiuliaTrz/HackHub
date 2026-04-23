package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.InvitoRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import com.project.hackhub.service.UserStateService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class InvitationReplyHandler {

    private final InvitoRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final UtenteRegistratoRepository userRepository;
    private final UserStateService userStateService;

    /**
     * Deletes the invitation for a user
     *
     * @param invitation the invitation
     * @throws IllegalArgumentException if the invitation is null
     * @author Giorgia Branchesi
     */
    private void removeInvitation(UUID invitation) {

        Invito i = invitationRepository.findById(invitation).orElseThrow(
                () -> new IllegalArgumentException("the invitation cannot be null"));

        Team t = i.getMittente();
        t.removeInvitationFromList(i);
        teamRepository.save(t);
        invitationRepository.delete(i);
    }

    /**
     * Accept invitation to take part on a team
     *
     * @param invitation the UUID of the invitation to accept
     * @throws IllegalArgumentException      if the invitation is {@code null}
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#IN_ISCRIZIONE}
     *                                       or if the user is not available, so they can't accept the invitation
     * @author Giorgia Branchesi
     */
    public void acceptInvitation(UUID invitation) {

        Invito i = invitationRepository.findById(invitation).orElseThrow(
                () -> new IllegalArgumentException("the invitation cannot be null"));

        UtenteRegistrato addressee = i.getDestinatario();
        Hackathon h = i.getMittente().getHackathon();

        if (!h.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE)
                || !addressee.isAvailable(h.getReservation()))
            throw new UnsupportedOperationException("cannot perform operation");

        addressee.removeInvitation(i);
        userStateService.changeUserState(addressee, true, h, UserStateType.MEMBRO_DEL_TEAM);
        i.getMittente().addTeamMember(addressee);
        removeInvitation(invitation);
    }

    /**
     * Declines invitation to take part on a team
     *
     * @param invitation the UUID of the invitation to decline
     * @throws IllegalArgumentException      if the invitation is {@code null}
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#IN_ISCRIZIONE}
     * @author Giorgia Branchesi
     */
    public void declineInvitation(UUID invitation) {

        Invito i = invitationRepository.findById(invitation).orElseThrow(
                () -> new IllegalArgumentException("the invitation cannot be null"));

        UtenteRegistrato addressee = i.getDestinatario();
        Hackathon h = i.getMittente().getHackathon();

        if (!h.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
            throw new UnsupportedOperationException("cannot perform operation");

        addressee.removeInvitation(i);
        userRepository.save(addressee);
        removeInvitation(invitation);
    }

}
