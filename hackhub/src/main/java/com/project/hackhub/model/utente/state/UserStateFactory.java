package com.project.hackhub.model.utente.state;

import java.util.Set;


public class UserStateFactory {

    public UserState createUserState(UserStateType st) {
        return switch (st) {
            case ORGANIZZATORE -> new Organizzatore();
            case MENTORE -> new Mentore();
            case GIUDICE -> new Giudice();
            case MEMBRO_DEL_TEAM -> new MembroDelTeam();
            case TEAM_LEADER -> new TeamLeader();
            case DEFAULT_STATE -> new DefaultState();
        };
    }
}
