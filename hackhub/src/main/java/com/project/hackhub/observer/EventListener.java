package com.project.hackhub.observer;

import com.project.hackhub.model.user.User;

import java.util.List;

public interface EventListener{

    /**
     * Gets the {@link EventType} supported by this listener
     * @return the {@link EventType} supported by this listener
     * @author Giorgia Branchesi
     */
    EventType getSupportedEventType();

    void updateUsers(List<User> usersList,
                     String message,
                     Object entity);


}
