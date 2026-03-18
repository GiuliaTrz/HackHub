package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.state.Giudice;
import com.project.hackhub.model.utente.state.Organizzatore;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Hackathon {

    private String name;
    private UUID id;
    private String description;
    private String ruleBook;
    private LocalDate expiredSubscriptionsDate;
    private int maxTeamDimension;
    private HackathonState state;
    private List<Team> teamsList;
    private List<UtenteRegistrato> mentorsList;
    private Soldi moneyPrice;
    private Giudice judge;
    private Organizzatore coordinator;
    private Prenotazione reservation;

    public void addMentor(UtenteRegistrato u){
        mentorsList.add(u);
    }

    public void setState(HackathonState hs){
        this.state = hs;
    }

    public HackathonState getState(){return this.state;}

    public boolean removeTeam(Team t){
        return teamsList.remove(t);
    }


}
