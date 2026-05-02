package com.project.hackhub.model.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.project.hackhub.model.hackathon.Hackathon;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable @NoArgsConstructor
@Getter
public class Infraction {

    @JsonIncludeProperties({"name", "id"})
    @ManyToOne
    @Getter
    private Team iTeam;
    private String iDescription;
    private String iType;
    private LocalDateTime timestamp;

    public Infraction(Team team, String description, String type){
        this.iTeam = team;
        this.iDescription = description;
        this.iType = type;
        this.timestamp = LocalDateTime.now();
    }

    @JsonIgnore
    public Hackathon getHackathon() {
        return this.iTeam.getHackathon();
    }
}
