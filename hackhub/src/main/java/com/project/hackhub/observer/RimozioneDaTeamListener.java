package com.project.hackhub.observer;

import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.List;

public class RimozioneDaTeamListener implements EventListener{
    @Override
    public void updateUsers(List<UtenteRegistrato> usersList, String message, Object entity) {
        //TODO
    }

    @Override
    public EventType getSupportedEventType() {
        //TODO
        return null;
    }
}
