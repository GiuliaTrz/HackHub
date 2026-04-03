package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class Giudice extends UserState {
    public Giudice() {
        super(EnumSet.of(Permission.CAN_GRADE_SUBMISSION, Permission.DETAILED_INFO));
    }

    @Override
    public void viewHackathonInfo() {

    }


}
