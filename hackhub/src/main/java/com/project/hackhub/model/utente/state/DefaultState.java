package com.project.hackhub.model.utente.state;

import java.util.EnumSet;

public class DefaultState extends UserState {

    public DefaultState() {
        super(EnumSet.of(Permission.CAN_CREATE_TEAM,
                Permission.DETAILED_INFO,
                Permission.CAN_ACCEPT_INVITATION,
                Permission.CAN_DECLINE_INVITATION));
    }

    @Override
    public UserStateType getType() {
        return UserStateType.DEFAULT_STATE;
    }
}
