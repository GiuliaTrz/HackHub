package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.observer.HackathonStateType;

public class Concluso implements HackathonState {
    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.CONCLUSO;
    }
}
