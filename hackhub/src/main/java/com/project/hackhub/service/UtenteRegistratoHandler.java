package com.project.hackhub.service;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserState;
import com.project.hackhub.model.utente.state.UserStateFactory;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UtenteRegistratoHandler {

    private final UtenteRegistratoRepository utenteRegistratoRepo;
    private final TeamRepository teamRepo;
    private final TeamHandler teamHandler;

    /**
     * Sets the given state on the given user and adds or removes the reservation of the Hackathon as needed
     *
     * @param user      the user which state is to update.
     * @param toAdd     {@code true} if the reservation needs to be added to the list of the user, {@code false} if it does not
     * @param hackathon the hackathon
     * @param state     the userState to set
     * @author Giorgia Branchesi
     */
    public void changeUserState(UtenteRegistrato user, boolean toAdd, Hackathon hackathon, UserStateType state) {

        UserStateFactory factory = new UserStateFactory();
        UserState concreteState = factory.createUserState(state);

        if (toAdd)
            user.setState(hackathon.getReservation(), concreteState);
        else {
            user.removeReservation(hackathon);
        }
        utenteRegistratoRepo.save(user);
    }

    public void addInvitation(UtenteRegistrato user, Invito invitation) {

        user.addInvitation(invitation);
        utenteRegistratoRepo.save(user);

    }

    /**
     * Accept invitation to take part on a team
     *
     * @param i the invitation to accept
     * @throws IllegalArgumentException      if the invitation is {@code null}
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#IN_ISCRIZIONE}
     *                                       or if the user is not available, so they can't accept the invitation
     * @author Giorgia Branchesi
     */
    public void acceptInvitation(Invito i) {

        if (i == null)
            throw new IllegalArgumentException("the invitation cannot be null");

        UtenteRegistrato addressee = i.getDestinatario();
        Hackathon h = i.getMittente().getHackathon();

        if (!h.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE)
                || !addressee.isAvailable(h.getReservation()))
            throw new UnsupportedOperationException("Azione non permessa");

        addressee.removeInvitation(i);
        changeUserState(addressee, true, h, UserStateType.MEMBRO_DEL_TEAM);
        teamHandler.acceptInvitation(i);
    }

    /**
     * Declines invitation to take part on a team
     *
     * @param i the invitation to decline
     * @throws IllegalArgumentException      if the invitation is {@code null}
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#IN_ISCRIZIONE}
     * @author Giorgia Branchesi
     */
    public void declineInvitation(Invito i) {

        if (i == null)
            throw new IllegalArgumentException("the invitation cannot be null");

        UtenteRegistrato addressee = i.getDestinatario();
        Hackathon h = i.getMittente().getHackathon();

        if (!h.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
            throw new UnsupportedOperationException("Azione non permessa");

        addressee.removeInvitation(i);
        utenteRegistratoRepo.save(addressee);
        teamHandler.removeInvitation(i);
    }

}
