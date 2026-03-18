package com.project.hackhub.model.hackathon.state;

public class InValutazione implements HackathonState {

    @Override
       public HackathonStateType getStateType() {
        return HackathonStateType.IN_VALUTAZIONE;
    }
}
