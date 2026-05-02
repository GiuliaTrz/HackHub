package com.project.hackhub.observer;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.UserStateType;
import com.project.hackhub.service.UserStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ChangeUserRoleListener implements EventListener {

    private final UserStateService userStateService;

    /**
     * {@inheritDoc}
     */
    @Override
    public EventType getSupportedEventType() {
        return EventType.CHANGE_STAFF_ROLE;
    }

    /**
     * Updates the users about the change of their role
     *
     * @param usersList the users to update
     * @param message   the message to send
     * @param entity    the entity
     * @throws IllegalArgumentException if the message or the entity are null
     * @author Giorgia Branchesi
     */
    @Override
    public void updateUsers(List<User> usersList, String message, Object entity) {

        if (usersList == null || usersList.isEmpty())
            return;
        if (message == null) throw new IllegalArgumentException("message needed");
        if (entity == null) throw new IllegalArgumentException("hackathon cannot be null");

        Hackathon hackathon = (Hackathon) entity;
        userStateService.changeUserState(usersList.getFirst(), false, hackathon, UserStateType.DEFAULT_STATE);
        // message will be simulated for testing through the API call
    }
}
