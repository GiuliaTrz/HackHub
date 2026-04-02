package com.project.hackhub.observer;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.team.Team;
import lombok.Getter;


public enum EventType {

    //le varie class sono da cambiare, le ho messe solo per reference
    ELIMINAZIONE_HACKATHON(Hackathon .class),
    NUOVO_LEADER(Hackathon .class),
    SCELTA_VINCITORE(Hackathon .class),
    RIMOZIONE_DA_TEAM(Hackathon .class),
    ELIMINAZIONE_TEAM(Hackathon .class),
    PROCLAMAZIONE_VINCITORE(Hackathon .class),
    ILLECITO(Hackathon .class),
    MODIFICA_HACKATHON(Hackathon .class),
    INVITO_UTENTE(Invito .class);

    @Getter private final Class<?> entityClass;

    EventType(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

}
