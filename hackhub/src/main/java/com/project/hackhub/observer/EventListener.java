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

    void updateUsers(List<UtenteRegistrato> usersList,
                     String message,
                     Object entity);


}
