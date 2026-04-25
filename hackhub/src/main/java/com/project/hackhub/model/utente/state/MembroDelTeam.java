package com.project.hackhub.model.utente.state;

import java.util.EnumSet;

public class MembroDelTeam extends UserState {


    public MembroDelTeam() {
        super(EnumSet.of(Permission.CAN_INVITE_USERS,
                Permission.DETAILED_INFO,
                Permission.TEAM_PERMISSION,
                Permission.CAN_CANCEL_INVITATION));
    }


    @Override
    public UserStateType getType() {
        return UserStateType.MEMBRO_DEL_TEAM;
    }
}
