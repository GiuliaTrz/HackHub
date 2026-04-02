package com.project.hackhub.observer;

import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.service.UtenteRegistratoHandler;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class InvitoUtenteListener implements EventListener{

    private final UtenteRegistratoHandler usersHandler;

    @Override
    public EventType getSupportedEventType() {
        return EventType.INVITO_UTENTE;
    }
    
    @Override
    public void updateUsers(List<UtenteRegistrato> usersList, String message, Object entity) {
        Invito invitation = (Invito) entity;
        usersHandler.addInvitation(usersList.getFirst(), invitation);
        System.out.println(message);
    }
}
