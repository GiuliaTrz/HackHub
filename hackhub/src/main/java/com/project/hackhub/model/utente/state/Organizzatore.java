package com.project.hackhub.model.utente.state;

import com.project.hackhub.dto.HackathonDTO;

public class Organizzatore implements UserState {
    @Override
    public void visualizzaHackathon() {

    }

    @Override
    public boolean hasPermission(Permission p) {
        return false;
    }

    public void insertData(HackathonDTO dto){

    }
}
