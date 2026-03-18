package com.project.hackhub.model.utente.state;

public class MembroDelTeam implements UserState {
    @Override
    public void visualizzaHackathon() {

    }

    @Override
    public boolean hasPermission(Permission p) {
        return false;
    }
}
