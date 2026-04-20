package com.project.hackhub.model.utente.state;


import java.util.EnumSet;

public class TeamLeader extends MembroDelTeam {

    public TeamLeader() {
        super(EnumSet.of(Permission.CAN_UNSUBSCRIBE_TEAM,
                Permission.CAN_SEND_AID_REQUEST,
                Permission.CAN_SEND_SUBMISSION));
    }
}
