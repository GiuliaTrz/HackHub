package com.project.hackhub.model.hackathon.state;

public class Concluso implements HackathonState {
    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.CONCLUSO;
    }
}
