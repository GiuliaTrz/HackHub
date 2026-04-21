package com.project.hackhub.observer;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.service.UserStateService;

import java.util.List;

public class SceltaVincitoreListener implements EventListener{

    @Override
    public EventType getSupportedEventType() {
        return EventType.SCELTA_VINCITORE;
    }

    @Override
    public void updateUsers(List<UtenteRegistrato> usersList, String message, Object entity) {
        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) throw new IllegalArgumentException("invitation cannot be null");

        Hackathon hackathon = (Hackathon) entity;
        for(UtenteRegistrato u: usersList) {
            //TODO scegliere come indicare la notifica ai partecipanti
        }
    }

}
