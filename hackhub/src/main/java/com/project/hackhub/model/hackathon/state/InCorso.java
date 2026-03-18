package com.project.hackhub.model.hackathon.state;

public class InCorso implements HackathonState {

    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.IN_CORSO;
    }
}
