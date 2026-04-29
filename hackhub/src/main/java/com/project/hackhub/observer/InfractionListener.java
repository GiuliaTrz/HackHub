package com.project.hackhub.observer;

import com.project.hackhub.model.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InfractionListener implements EventListener{

    /**
     * Gets the {@link EventType} supported by this listener
     * @return the {@link EventType} supported by this listener
     * @author Giorgia Branchesi
     */
    @Override
    public EventType getSupportedEventType() {
        return EventType.INFRACTION;
    }

    /**
     * Updates the coordinator about an infraction to handle
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
        if(entity == null) throw new IllegalArgumentException("hackathon cannot be null");
        // message will be simulated for testing through the API call
    }
}
