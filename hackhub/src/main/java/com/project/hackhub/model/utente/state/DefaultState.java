package com.project.hackhub.model.utente.state;

import java.util.EnumSet;
import java.util.List;

public class DefaultState extends UserState {

    public DefaultState() {
        super(EnumSet.of(Permission.CAN_CREATE_TEAM));
    }


    @Override
    public void viewHackathonInfo() {

    }
}
