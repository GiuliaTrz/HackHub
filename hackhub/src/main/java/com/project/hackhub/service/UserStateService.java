package com.project.hackhub.service;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Invitation;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.UserStateType;
import com.project.hackhub.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class UserStateService {

    private final UserRepository userRepository;

    /**
     * Sets the given state on the given user and adds or removes the reservation of the Hackathon as needed
     *
     * @param user      the user which state is to update.
     * @param toAdd     {@code true} if the reservation needs to be added to the list of the user, {@code false} if it does not
     * @param hackathon the hackathon
     * @param state     the userState to set
     * @throws IllegalArgumentException if any of the parameters are null
     * @author Giorgia Branchesi
     */
    public void changeUserState(User user, boolean toAdd, Hackathon hackathon, UserStateType state) {

        if (user == null) throw new IllegalArgumentException("user cannot be null");
        if (hackathon == null) throw new IllegalArgumentException("hackathon cannot be null");
        if (state == null) throw new IllegalArgumentException("state cannot be null");

        if (toAdd)
            user.setState(hackathon.getReservation(), state);
        else {
            user.removeReservation(hackathon.getReservation());
        }
        userRepository.save(user);
    }

    public void addInvitation(User user, Invitation invitation) {

        user.addInvitation(invitation);
        userRepository.save(user);

    }

}
