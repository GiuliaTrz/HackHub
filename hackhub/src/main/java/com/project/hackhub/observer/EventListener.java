package com.project.hackhub.observer;

import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.List;

public interface EventListener{

    /**
     * Gets the {@link EventType} supported by this listener
     * @return the {@link EventType} supported by this listener
     * @author Giorgia Branchesi
     */
    EventType getSupportedEventType();

    /**
     * Updates the users about the expulsion of their team from the Hackathon
     *
     * @param usersList the users to update
     * @param message the message to send
     * @param entity the entity
     * @throws IllegalArgumentException if the message or the entity are null
     * @author Giorgia Branchesi
     */
    void updateUsers(List<UtenteRegistrato> usersList,
                     String message,
                     Object entity);


}
