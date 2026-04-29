package com.project.hackhub.observer;

import com.project.hackhub.model.team.Invitation;
import com.project.hackhub.model.user.User;
import com.project.hackhub.service.UserStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserInvitationListener implements EventListener{

    private final UserStateService userStateService;

    /**
     * {@inheritDoc}
     */
    @Override
    public EventType getSupportedEventType() {
        return EventType.USER_INVITATION;
    }

    /**
     * Updates the users about its invitation on a team
     *
     * @param usersList the users to update
     * @param message the message to send
     * @param entity the entity
     * @throws IllegalArgumentException if the message or the entity are null
     * @author Giorgia Branchesi
     */
    @Override
    public void updateUsers(List<User> usersList, String message, Object entity) {

        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) throw new IllegalArgumentException("invitation cannot be null");

        Invitation invitation = (Invitation) entity;
        userStateService.addInvitation(usersList.getFirst(), invitation);
        // message will be simulated for testing through the API call
    }
}
