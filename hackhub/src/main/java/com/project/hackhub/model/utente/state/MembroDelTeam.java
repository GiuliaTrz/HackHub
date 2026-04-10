package com.project.hackhub.model.utente.state;

import java.util.EnumSet;
import java.util.List;

public class MembroDelTeam extends UserState {


    public MembroDelTeam() {
        super(EnumSet.of(Permission.CAN_INVITE_USERS, Permission.DETAILED_INFO,
                Permission.TEAM_PERMISSION));
    }

    protected MembroDelTeam(EnumSet<Permission> permissions) {
        super(permissions);
    }


}
