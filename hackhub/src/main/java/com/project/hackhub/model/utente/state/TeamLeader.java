package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.Set;

public class TeamLeader extends MembroDelTeam {

    public TeamLeader(Set<Permission> permissions) {
        super(permissions);
    }
}
