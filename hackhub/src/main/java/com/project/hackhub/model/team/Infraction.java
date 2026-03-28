package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable @NoArgsConstructor
public class Infraction {

    @ManyToOne
    private Team iTeam;

    private String iDescription;
    //Si potrebbe anche mettere una enum per i tipi di infrazione volendo
    private String iType;
    private LocalDateTime timestamp;

    public Infraction(UUID id, Team team, String description, String type){
        this.iTeam = team;
        this.iDescription = description;
        this.iType = type;
        this.timestamp = LocalDateTime.now();
    }

    public Hackathon getHackathon() {
        return this.iTeam.getHackathon();
    }
}
