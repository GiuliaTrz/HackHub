package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;

public class AidRequest {
    private String description;
    private String type;
    private Team team;

    public AidRequest(Team t, String type){
        this.team = t;
        this.type = type;
    }

    public Hackathon getHackathon() {
        return this.team.getHackathon();
    }
}
