package com.project.hackhub.model.user.state;

import java.util.EnumSet;

public class Coordinator extends UserState {

    public Coordinator() {
        super(EnumSet.of(Permission.CAN_MODIFY_HACKATHON,
                Permission.CAN_MANAGE_INFRACTIONS,
                Permission.CAN_ADD_MENTOR,
                Permission.DETAILED_INFO,
                Permission.STAFF_PERMISSION,
                Permission.CAN_PROCLAIM_WINNER,
                Permission.CAN_ADD_TASK,
                Permission.CAN_EXPEL_TEAM,
                Permission.CAN_PENALIZE_TEAM,
                Permission.CAN_DELETE_HACKATHON
                ));

    }

    @Override
    public UserStateType getType() {
        return UserStateType.COORDINATOR;
    }
}
