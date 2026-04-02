package com.project.hackhub.model.utente;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.state.Permission;

public class Visitatore implements Utente{

    public boolean createAccount(Anagrafica a){
        return true;
    }

    @Override
    public boolean hasPermission(Permission p, Hackathon h) {
        return false;
    }
}
