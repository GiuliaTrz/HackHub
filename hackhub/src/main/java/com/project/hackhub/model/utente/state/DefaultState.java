package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.team.Infraction;

import java.util.List;
import java.util.Set;

public class DefaultState extends UserState {

    public DefaultState(Set<Permission> permissions) {
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
        return null;
    }
}
