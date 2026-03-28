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

    public AidRequest(Team t, String type){
        this.team = t;
        this.type = type;
    }

    public Hackathon getHackathon() {
        return this.team.getHackathon();
    }
}
