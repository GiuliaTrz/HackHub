package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.AidRequest;

public interface MentorshipActions {
    boolean addPendingRequest(String slot, AidRequest aidRequest);
    boolean handleRequest(AidRequest aidRequest);
    boolean addInfraction(Infraction infraction);
}
