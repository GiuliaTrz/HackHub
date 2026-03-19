package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Hackathon {

    private String name;
    private UUID id;
    private String ruleBook;
    private LocalDate expiredSubscriptionsDate;
    private int maxTeamDimension;
    private HackathonState state;
    private List<Team> teamsList;
    private List<UtenteRegistrato> mentorsList;
    private Soldi moneyPrice;
    private UtenteRegistrato judge;
    private UtenteRegistrato coordinator;
    private Prenotazione reservation;



    public void addMentor(UtenteRegistrato u){
        mentorsList.add(u);
    }

    public boolean removeTeam(Team t){
        teamsList.remove(t);
        return true;
    }


}
