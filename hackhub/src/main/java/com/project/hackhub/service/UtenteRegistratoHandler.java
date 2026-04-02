package com.project.hackhub.service;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserState;
import com.project.hackhub.model.utente.state.UserStateFactory;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UtenteRegistratoHandler {

    private final UtenteRegistratoRepository utenteRegistratoRepo;
    /**
     * Sets the given state on the given user and adds or removes the reservation of the Hackathon as needed
     *
     * @param user the user which state is to update.
     * @param toAdd {@code true} if the reservation needs to be added to the list of the user, {@code false} if it does not
     * @param hackathon the hackathon
     * @param state the userState to set
     *
     * @author Giorgia Branchesi
     */
    public void changeUserState(UtenteRegistrato user, boolean toAdd, Hackathon hackathon, UserStateType state) {

        UserStateFactory factory = new UserStateFactory();
        UserState concreteState = factory.createUserState(state);

        if(toAdd)
            user.setState(hackathon.getReservation(), concreteState);
        else
        {
            user.removeReservation(hackathon);
        }
    }

    public void addInvitation(UtenteRegistrato user, Invito invitation) {

        user.addInvitation(invitation);
        utenteRegistratoRepo.save(user);

    }
}
