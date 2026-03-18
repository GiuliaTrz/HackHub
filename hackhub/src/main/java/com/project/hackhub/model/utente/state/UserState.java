package com.project.hackhub.model.utente.state;

public interface UserState {

    void visualizzaHackathon();

    boolean hasPermission(Permission p);
}
