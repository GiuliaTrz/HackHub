package com.project.hackhub.model.user.state;

import java.util.EnumSet;

public class TeamMember extends UserState {


    public TeamMember() {
        super(EnumSet.of(Permission.CAN_INVITE_USERS,
                Permission.DETAILED_INFO,
                Permission.TEAM_PERMISSION,
                Permission.CAN_CANCEL_INVITATION));
    }


    @Override
    public UserStateType getType() {
        return UserStateType.TEAM_MEMBER;
    }
}
