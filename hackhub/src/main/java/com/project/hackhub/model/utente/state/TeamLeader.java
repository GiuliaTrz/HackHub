package com.project.hackhub.model.utente.state;


import java.util.EnumSet;

public class TeamLeader extends UserState {

    public TeamLeader() {
        super(EnumSet.of(Permission.CAN_INVITE_USERS,
                Permission.DETAILED_INFO,
                Permission.TEAM_PERMISSION,
                Permission.CAN_CANCEL_INVITATION,
                Permission.CAN_UNSUBSCRIBE_TEAM,
                Permission.CAN_SEND_AID_REQUEST,
                Permission.CAN_SEND_SUBMISSION,
                Permission.CAN_MODIFY_LEADER));
    }

    @Override
    public UserStateType getType() {
        return UserStateType.TEAM_LEADER;
    }
}