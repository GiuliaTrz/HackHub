package com.project.hackhub.model.utente.state;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.team.Infraction;
import java.util.Collections;
import java.util.List;

public interface CoordinatorActions {
    default void handleInfractions(Infraction infraction) {
        throw new UnsupportedOperationException("Azione non permessa.");
    };

    default void insertData(HackathonDTO dto){
        throw new UnsupportedOperationException("Azione non permessa.");
    }
}
