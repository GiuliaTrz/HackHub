package com.project.hackhub.observer;

import com.project.hackhub.model.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PenalizedTeamListener implements EventListener{

    /**  {@inheritDoc}     */
    @Override
    public EventType getSupportedEventType() {
        return EventType.PENALIZED_TEAM;
    }

    /**
     * Updates the users about the penalization of their team final grade
     *
     * @param usersList the users to update
     * @param message the message to send
     * @param entity the entity
     * @throws IllegalArgumentException if the message or the entity are null
     *
     * @author Giorgia Branchesi
     */
    @Override
    public void updateUsers(List<User> usersList, String message, Object entity) {

        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) throw new IllegalArgumentException("hackathon cannot be null");

        // message will be simulated for testing through the API call
    }
}
