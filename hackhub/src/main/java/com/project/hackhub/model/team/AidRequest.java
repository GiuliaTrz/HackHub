package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.spi.ToolProvider;

@Embeddable
@NoArgsConstructor
public class AidRequest {

    private String description;
    private AidRequestType type;

    @OneToOne
    private Team team;

    public AidRequest(Team t, AidRequestType type, String description){
        if (t == null || type == null)
            throw new IllegalArgumentException("Invalid parameters.");

        this.team = t;
        this.type = type;
        this.description = description;
    }

    public AidRequest(Team t, AidRequestType type){
        this(t, type, null);
    }

    public Hackathon getHackathon() {
        if (team == null)
            throw new IllegalStateException("Team not associated.");
        return team.getHackathon();
    }

    public Team getTeam() {
        return this.team;
    }

}