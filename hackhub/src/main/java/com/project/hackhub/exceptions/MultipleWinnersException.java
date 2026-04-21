package com.project.hackhub.exceptions;

import com.project.hackhub.model.team.Team;

import java.util.List;

public class MultipleWinnersException extends RuntimeException {
    private final List<Team> tiedTeams;
    public MultipleWinnersException(List<Team> tiedTeams) {
        super("Multiple teams have the same maximum grade. A manual choice is required.");
        this.tiedTeams = tiedTeams;
    }
    public List<Team> getTiedTeams() {
        return tiedTeams;
    }
}
