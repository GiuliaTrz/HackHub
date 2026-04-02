package com.project.hackhub.observer;

import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.service.UtenteRegistratoHandler;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class InvitoUtenteListener implements EventListener{

    private final UtenteRegistratoHandler usersHandler;

    /**
     * Gets the {@link EventType} supported by this listener
     * @return the {@link EventType} supported by this listener
     * @author Giorgia Branchesi
     */
    @Override
    public EventType getSupportedEventType() {
        return EventType.INVITO_UTENTE;
    }

    /**
     * Updates the users about its invitation on a team
     *
     * @param usersList the users to update
     * @param message the message to send
     * @param entity the entity
     * @throws IllegalArgumentException if the message or the entity are null
     * @author Giorgia Branchesi
     */
    @Override
    public void updateUsers(List<UtenteRegistrato> usersList, String message, Object entity) {

        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) throw new IllegalArgumentException("invitation cannot be null");

        Invito invitation = (Invito) entity;
        usersHandler.addInvitation(usersList.getFirst(), invitation);
        //TODO, il messaggio è da restituire quando facciamo la chiamata API!
    }
}
