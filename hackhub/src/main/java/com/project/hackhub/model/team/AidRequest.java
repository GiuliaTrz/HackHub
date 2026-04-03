package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;

@Embeddable @NoArgsConstructor
public class AidRequest {

    private String description;
    private String type;

    @OneToOne
    private Team team;

<<<<<<< Updated upstream
    public AidRequest(Team t, String type){
        this.team = t;
        this.type = type;
=======
    public AidRequest(Team t, AidRequestType type, String description){
        if (t == null || type == null)
            throw new IllegalArgumentException("Parametri non validi.");

        this.team = t;
        this.type = type;
        this.description = description;
    }

    public AidRequest(Team t, AidRequestType type){
        this(t, type, null);
>>>>>>> Stashed changes
    }

    public Hackathon getHackathon() {
        if (team == null)
            throw new IllegalStateException("Team non associato.");
        return team.getHackathon();
    }
<<<<<<< Updated upstream
}
=======

    public Team getTeam() {
        return this.team;
    }
}
>>>>>>> Stashed changes
