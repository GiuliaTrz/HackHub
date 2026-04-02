package com.project.hackhub.observer;

import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.List;

public interface EventListener{

    void updateUsers(List<UtenteRegistrato> usersList,
                     String message,
                     Object entity);

    EventType getSupportedEventType();
}
