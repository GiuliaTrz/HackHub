package com.project.hackhub.model.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.project.hackhub.model.hackathon.Hackathon;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Infraction that)) return false;
        return Objects.equals(iTeam, that.iTeam) && Objects.equals(iDescription, that.iDescription) && Objects.equals(iType, that.iType) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iTeam, iDescription, iType, timestamp);
    }
}
