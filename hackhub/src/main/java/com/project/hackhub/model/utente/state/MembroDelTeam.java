package com.project.hackhub.model.utente.state;

import java.util.EnumSet;
import java.util.List;

public class MembroDelTeam extends UserState {


    public MembroDelTeam() {
        super(EnumSet.of(Permission.CAN_INVITE_USERS));
    }

    protected MembroDelTeam(EnumSet<Permission> permissions) {
        super(permissions);
    }

    //TODO
    @Override
    public void viewHackathonInfo() {

    }
}
