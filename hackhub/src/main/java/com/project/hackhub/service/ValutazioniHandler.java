package com.project.hackhub.service;

import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import lombok.NonNull;

public class ValutazioniHandler {

    public void handleInfractions(@ NonNull UtenteRegistrato utenteRegistrato, @NonNull Infraction infraction){
        if(!utenteRegistrato.hasPermission(Permission.CAN_MANAGE_INFRACTIONS, infraction.getHackathon()))
            throw new UnsupportedOperationException("Azione non permessa.");
        //TODO
    }

    public boolean addInfraction(@NonNull UtenteRegistrato utenteRegistrato, @NonNull Infraction infraction){
        if(!utenteRegistrato.hasPermission(Permission.CAN_REPORT_INFRACTION, infraction.getHackathon()))
            throw new UnsupportedOperationException("Azione non permessa.");
        infraction.getHackathon().getInfractions().add(infraction);
        return true;

    }
}
