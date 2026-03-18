package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.observer.HackathonStateType;

public class InValutazione implements HackathonState {

    @Override
       public HackathonStateType getStateType() {
        return HackathonStateType.IN_VALUTAZIONE;
    }
}
