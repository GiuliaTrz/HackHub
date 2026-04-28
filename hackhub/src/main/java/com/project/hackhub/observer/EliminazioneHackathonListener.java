package com.project.hackhub.observer;

import com.project.hackhub.model.utente.UtenteRegistrato;
import org.springframework.stereotype.Component;

import java.util.List;



public class EliminazioneHackathonListener implements EventListener{
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
