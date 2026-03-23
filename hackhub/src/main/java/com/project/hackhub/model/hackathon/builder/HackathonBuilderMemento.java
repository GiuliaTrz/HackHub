package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Hackathon;

public class HackathonBuilderMemento implements Memento {

    private final Hackathon hackathon;

    public HackathonBuilderMemento(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    @Override
    public Hackathon getState() {
        return hackathon;
    }

}
