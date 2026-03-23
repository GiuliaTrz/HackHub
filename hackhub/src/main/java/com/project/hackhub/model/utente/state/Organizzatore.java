package com.project.hackhub.model.utente.state;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.List;
import java.util.Set;

public class Organizzatore extends UserState {

    public Organizzatore(Set<Permission> permissions) {
        super(permissions);
    }

    @Override
    public void viewHackathon() {

    }

    @Override
    public boolean hasPermission(Permission p) {
        return false;
    }

    @Override
    public List<String> getInfractions() {
        return List.of();
    }

    public void handleInfractions(Infraction infraction) {

    }

    public boolean addInfraction(Infraction infraction) {
        return false;
    }

    public void insertData(HackathonDTO dto){

    }
}
