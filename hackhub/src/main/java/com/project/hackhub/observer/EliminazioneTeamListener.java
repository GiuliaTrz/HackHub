package com.project.hackhub.observer;

import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;

import java.util.List;

import static com.project.hackhub.service.UserStateService.changeUserState;

public class EliminazioneTeamListener implements EventListener{

    /**
     * Gets the {@link EventType} supported by this listener
     * @return the {@link EventType} supported by this listener
     * @author Giorgia Branchesi
     */
    @Override
    public EventType getSupportedEventType() {
        return EventType.ELIMINAZIONE_TEAM;
    }

    /**
     * Updates the users about the elimination of the team
     *
     * @param usersList the users to update
     * @param message the message to send
     * @param entity the entity
     * @throws IllegalArgumentException if the message or the entity are null
     *
     * @author Giorgia Branchesi
     */
    @Override
    public void updateUsers(List<UtenteRegistrato> usersList, String message, Object entity) {

        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) throw new IllegalArgumentException("team cannot be null");

        Team team = (Team) entity;
        for(UtenteRegistrato u: usersList) {
            changeUserState(u, false, team.getHackathon(), UserStateType.DEFAULT_STATE);
        }
        //TODO, il messaggio è da restituire quando facciamo la chiamata API! Gli utenti da notificare sono tutti i membri del team
    }
}
