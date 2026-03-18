package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.observer.HackathonStateType;

public class InIscrizione implements HackathonState {
    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.IN_ISCRIZIONE;
    }
}
