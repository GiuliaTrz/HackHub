package com.project.hackhub.model.utente.state;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class Organizzatore extends UserState {

    public Organizzatore() {
        super(EnumSet.of(Permission.CAN_MODIFY_HACKATHON,
                Permission.CAN_MANAGE_INFRACTIONS,
                Permission.CAN_ADD_MENTOR,
                Permission.CAN_ADD_JUDGE,
                Permission.DETAILED_INFO));
    }

}
