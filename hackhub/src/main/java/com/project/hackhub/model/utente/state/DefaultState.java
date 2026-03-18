package com.project.hackhub.model.utente.state;

public class DefaultState implements UserState {
    @Override
    public void visualizzaHackathon() {

    }

    @Override
    public boolean hasPermission(Permission p) {
        return false;
    }
}
