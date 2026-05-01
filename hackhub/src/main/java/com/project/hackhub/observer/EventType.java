package com.project.hackhub.observer;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Invitation;
import com.project.hackhub.model.team.Team;
import lombok.Getter;


public enum EventType {

    HACKATHON_DELETION(Hackathon .class),
    REMOVED_MEMBER_FROM_TEAM(Hackathon .class),
    MODIFIED_HACKATHON(Hackathon .class),
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
