package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.List;
import java.util.Set;

public abstract class UserState {
    protected Set<Permission> permissions;


    public UserState(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(Permission p) {
        return permissions.contains(p);
    }

    public abstract void viewHackathon();

    //implementato da Giudice, Mentore e Organizzatore
    public abstract List<String> getInfractions();



}
