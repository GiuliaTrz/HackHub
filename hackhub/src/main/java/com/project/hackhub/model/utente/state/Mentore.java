package com.project.hackhub.model.utente.state;

import java.util.EnumSet;

public class Mentore extends UserState {


    public Mentore(){
        super(EnumSet.of(Permission.CAN_REPORT_INFRACTION,
                Permission.CAN_PROPOSE_CALL, Permission.DETAILED_INFO,
                Permission.STAFF_PERMISSION));
    }


    @Override
    public UserStateType getType() {
        return UserStateType.MENTORE;
    }
}
