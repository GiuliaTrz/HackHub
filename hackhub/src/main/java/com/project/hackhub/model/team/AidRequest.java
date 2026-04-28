package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.service.calendar.Slot;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class AidRequest {

    private String description;
    private AidRequestType type;
    @Embedded
    private Slot slot;

    @ManyToOne @Getter
    private Team team;

    public AidRequest(Team t, AidRequestType type, String description, Slot slot){
        if (t == null || type == null)
            throw new IllegalArgumentException("Invalid parameters.");
        this.team = t;
        this.type = type;
        this.description = description;
        this.slot = slot;
    }

    public AidRequest(Team t, AidRequestType type, Slot slot){
        this(t, type, null, slot);
    }

    public Hackathon getHackathon() {
        if (team == null)
            throw new IllegalStateException("Team not associated.");
        return team.getHackathon();
    }
}