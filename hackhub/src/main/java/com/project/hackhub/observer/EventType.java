package com.project.hackhub.observer;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Invitation;
import com.project.hackhub.model.team.Team;
import lombok.Getter;


public enum EventType {

    //le varie class sono da cambiare, le ho messe solo per reference
    ELIMINAZIONE_HACKATHON(Hackathon .class),
    RIMOZIONE_DA_TEAM(Hackathon .class),
    MODIFICA_HACKATHON(Hackathon .class),

    //giuste
    INFRACTION(Hackathon .class),
    PROCLAIM_WINNER(Hackathon .class),
    EXPULSION_TEAM(Hackathon .class),
    UNSUBSCRIBE_TEAM(Team .class),
    NEW_LEADER(Team .class),
    USER_INVITATION(Invitation.class),
    PENALIZED_TEAM(Hackathon .class),
    WINNER_CHOICE(Hackathon .class);

    @Getter private final Class<?> entityClass;

    EventType(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

}
