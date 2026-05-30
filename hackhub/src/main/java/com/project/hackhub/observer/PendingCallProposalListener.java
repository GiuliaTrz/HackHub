package com.project.hackhub.observer;

import com.project.hackhub.model.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PendingCallProposalListener implements EventListener{

    /**
     * Gets the {@link EventType} supported by this listener
     * @return the {@link EventType} supported by this listener
     * @author Giorgia Branchesi
     */
    @Override
    public EventType getSupportedEventType() {
        return EventType.PENDING_CALL_PROPOSAL;
    }

    @Override
    public void updateUsers(List<User> usersList, String message, Object entity) {

        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) throw new IllegalArgumentException("hackathon cannot be null");
        // message will be simulated for testing through the API call
    }
}
