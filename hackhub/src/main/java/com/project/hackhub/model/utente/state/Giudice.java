package com.project.hackhub.model.utente.state;

import java.util.EnumSet;

public class Giudice extends UserState {
    public Giudice() {
        super(EnumSet.of(Permission.CAN_GRADE_SUBMISSION,
                Permission.DETAILED_INFO,
                Permission.STAFF_PERMISSION));

    }


}
