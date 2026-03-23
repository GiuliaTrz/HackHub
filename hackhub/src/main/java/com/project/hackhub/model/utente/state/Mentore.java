package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.AidRequest;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Mentore extends UserState implements MentorshipActions {
    private UtenteRegistrato utenteRegistrato;
    private Hackathon hackathon;
    private List<AidRequest> pendingRequests;

    public Mentore(UtenteRegistrato u, Hackathon h){
        super(Set.of(
                Permission.CAN_REPORT_INFRACTION
        ));
        this.utenteRegistrato = u;
        this.hackathon = h;
        this.pendingRequests = new ArrayList<AidRequest>();
    }

    @Override
    public boolean hasPermission(Permission p) {
        return switch(p) {
            case Permission.CAN_REPORT_INFRACTION -> true;
            default -> false;
        };
    }


    @Override
    public void viewHackathon() {

    }

    @Override
    public List<String> getInfractions() {
        return List.of();
    }

    @Override
    public boolean addInfraction(Infraction infraction) {
        return false;
    }

    @Override
    public boolean addPendingRequest(String slot, AidRequest aidRequest) {
        return false;
    }

    @Override
    public boolean handleRequest(AidRequest aidRequest) {
        return false;
    }
}
