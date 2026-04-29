package com.project.hackhub.observer;

import com.project.hackhub.model.user.User;

import java.util.List;



public class EliminazioneHackathonListener implements EventListener{
    @Override
    public void updateUsers(List<User> usersList, String message, Object entity) {
        //TODO
    }

    @Override
    public EventType getSupportedEventType() {
        //TODO
        return null;
    }
}
