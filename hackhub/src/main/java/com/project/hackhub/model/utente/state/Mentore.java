package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.AidRequest;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class Mentore extends UserState {

    private List<AidRequest> pendingRequests;

    public Mentore(){
        super(EnumSet.of(Permission.CAN_REPORT_INFRACTION,
                Permission.CAN_PROPOSE_CALL));
    }


    //TODO
    @Override
    public void viewHackathon() {

    }
    //TODO
    @Override
    public List<String> getInfractions() {
        return List.of();
    }

    //TODO
    @Override
    public boolean addInfraction(Infraction infraction) {
        return true;
    }

    //TODO
    @Override
    public boolean addPendingRequest(String slot, AidRequest aidRequest) {
        return true;
    }

    //TODO
    @Override
    public boolean handleRequest(AidRequest aidRequest) {
        return false;
    }
}
