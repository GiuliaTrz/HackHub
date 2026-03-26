package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.EnumSet;
import java.util.Set;

public class TeamLeader extends MembroDelTeam {

    public TeamLeader() {
        super(EnumSet.of(Permission.CAN_UNSUBSCRIBE_TEAM));
    }
}
