package com.project.hackhub.model.hackathon.state;


import jakarta.persistence.Embeddable;

@Embeddable
public interface HackathonState {

    HackathonStateType getStateType();
}

