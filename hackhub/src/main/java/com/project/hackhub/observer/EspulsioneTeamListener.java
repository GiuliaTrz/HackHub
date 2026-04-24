package com.project.hackhub.observer;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.service.UserStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EspulsioneTeamListener implements EventListener {

    private final UserStateService userStateService;

    /** {@inheritDoc} */
    @Override
    public EventType getSupportedEventType() {
        return EventType.ESPULSIONE_TEAM;
    }

    /** {@inheritDoc} */
    @Override
    public void updateUsers(List<UtenteRegistrato> usersList, String message, Object entity) {

        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) throw new IllegalArgumentException("hackathon cannot be null");

        Hackathon hackathon = (Hackathon) entity;
        for(UtenteRegistrato u: usersList) {
            userStateService.changeUserState(u, false, hackathon, UserStateType.DEFAULT_STATE);
        }
        //TODO, il messaggio è da restituire quando facciamo la chiamata API!
    }
}
