package com.project.hackhub.observer;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.service.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EliminazioneHackathonListener implements EventListener {

    private final UserStateService userStateService;

    @Override
    public void updateUsers(List<UtenteRegistrato> usersList, String message, Object entity) {
        if (!(entity instanceof Hackathon hackathon)) {
            throw new IllegalArgumentException("Entity must be a Hackathon");
        }

        for (UtenteRegistrato user : usersList) {
            userStateService.changeUserState(user, false, hackathon, UserStateType.DEFAULT_STATE);
        }
    }

    @Override
    public EventType getSupportedEventType() {
        return EventType.ELIMINAZIONE_HACKATHON;
    }
}