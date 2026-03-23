package com.project.hackhub.model.team;

import java.time.LocalDateTime;
import java.util.UUID;

public class Infraction {

    private UUID id;
    private Team team;
    private String description;
    //Si potrebbe anche mettere una enum per i tipi di infrazione volendo
    private String type;
    private LocalDateTime timestamp;

    public Infraction(UUID id, Team team, String description, String type){
        this.id = id;
        this.team = team;
        this.description = description;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
}
