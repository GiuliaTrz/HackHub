package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.team.Infraction;

public interface CoordinatorActions {
    void handleInfractions(Infraction infraction);
}
