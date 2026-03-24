package com.project.hackhub.model.utente.state;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class UserState implements MentorshipActions, CoordinatorActions{
    protected Set<Permission> permissions;


    protected UserState(Set<Permission> permissions) {
        this.permissions = (permissions == null)? Collections.emptySet() : permissions;
    }

    public boolean hasPermission(Permission p) {
        return permissions.contains(p);
    }

    //TODO
    //implementato da Giudice, Mentore e Organizzatore
    public abstract List<String> getInfractions();


    //TODO
    //comune
    public abstract void viewHackathon();


}
