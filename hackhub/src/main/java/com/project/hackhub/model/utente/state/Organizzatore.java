package com.project.hackhub.model.utente.state;

import java.util.EnumSet;

public class Organizzatore extends UserState {

    public Organizzatore() {
        super(EnumSet.of(Permission.CAN_MODIFY_HACKATHON,
                Permission.CAN_MANAGE_INFRACTIONS,
                Permission.CAN_ADD_MENTOR,
                Permission.CAN_ADD_JUDGE,
                Permission.DETAILED_INFO,
                Permission.STAFF_PERMISSION));

    }

}
