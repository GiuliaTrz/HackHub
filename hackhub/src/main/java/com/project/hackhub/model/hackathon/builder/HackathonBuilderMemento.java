package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.utente.state.Giudice;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.List;

public class HackathonBuilderMemento implements Memento {

    private Giudice judge;

    private List<UtenteRegistrato> mentorsList;

    @Override
    public HackathonBuilderMemento restoreMemento() {
        return null;
    }
}
