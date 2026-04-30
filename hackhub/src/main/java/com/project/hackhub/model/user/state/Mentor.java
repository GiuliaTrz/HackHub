package com.project.hackhub.model.user.state;

import java.util.EnumSet;

public class Mentor extends UserState {


    public Mentor(){
        super(EnumSet.of(Permission.CAN_REPORT_INFRACTION,
                Permission.CAN_PROPOSE_CALL, Permission.DETAILED_INFO,
                Permission.STAFF_PERMISSION,
                Permission.CAN_DELETE_INFRACTION));
    }


    @Override
    public UserStateType getType() {
        return UserStateType.MENTOR;
    }
}
