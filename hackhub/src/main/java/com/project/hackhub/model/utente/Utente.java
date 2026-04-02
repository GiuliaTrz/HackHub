package com.project.hackhub.model.utente;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.state.Permission;

public interface Utente {
    boolean hasPermission(Permission p, Hackathon h);
}
