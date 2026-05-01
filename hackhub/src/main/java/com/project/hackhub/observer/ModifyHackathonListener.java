package com.project.hackhub.observer;

import com.project.hackhub.model.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModifyHackathonListener implements EventListener{


    @Override
    public void updateUsers(List<User> usersList, String message, Object entity) {

        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

    }

    @Override
    public EventType getSupportedEventType() {
         return EventType.MODIFIED_HACKATHON;
    }
}
