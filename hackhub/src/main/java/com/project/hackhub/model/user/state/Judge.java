package com.project.hackhub.model.user.state;

import java.util.EnumSet;

public class Judge extends UserState {
    public Judge() {
        super(EnumSet.of(Permission.CAN_GRADE_SUBMISSION,
                Permission.DETAILED_INFO,
                Permission.STAFF_PERMISSION,
                Permission.CAN_CHOOSE_WINNER));

    }


    @Override
    public UserStateType getType() {
        return UserStateType.JUDGE;
    }
}
