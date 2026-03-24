package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.utente.AidRequest;

public interface MentorshipActions {
    default boolean addPendingRequest(String slot, AidRequest aidRequest){
        return false;
    };
    default boolean handleRequest(AidRequest aidRequest){
        throw new UnsupportedOperationException("Azione non permessa.");
    };
    default boolean addInfraction(Infraction infraction){
        return false;
    };
}
