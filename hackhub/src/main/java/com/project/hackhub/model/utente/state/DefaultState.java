package com.project.hackhub.model.utente.state;

import java.util.EnumSet;

public class DefaultState extends UserState {

    public DefaultState() {
        super(EnumSet.of(Permission.CAN_CREATE_TEAM,
                Permission.DETAILED_INFO));
    }

}
