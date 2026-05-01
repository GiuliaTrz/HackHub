package com.project.hackhub.observer;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.UserStateType;
import com.project.hackhub.service.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HackathonDeletionListener implements EventListener {

    private final UserStateService userStateService;

    @Override
    public void updateUsers(List<User> usersList, String message, Object entity) {

        if(usersList == null || usersList.isEmpty())
            return;
        if(message == null) throw new IllegalArgumentException("message needed");
        if(entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        Hackathon hackathon = (Hackathon) entity;
        for (User user : usersList) {
            userStateService.changeUserState(user, false, hackathon, UserStateType.DEFAULT_STATE);
        }
    }

    @Override
    public EventType getSupportedEventType() {
        return EventType.HACKATHON_DELETION;
    }
}