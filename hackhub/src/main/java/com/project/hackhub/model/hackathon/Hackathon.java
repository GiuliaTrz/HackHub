package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.AidRequest;
import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter @Getter
@NoArgsConstructor
public class Hackathon {

    private UUID id;
    private String name;
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
    private List<Infraction> infractions;
    private List<AidRequest> aidRequests;

    //costruttore di copia!
    public Hackathon(Hackathon other) {
        this.name = other.name;
        this.ruleBook = other.ruleBook;
        this.maxTeamDimension = other.maxTeamDimension;
        this.reservation = other.reservation;
        this.moneyPrice = other.moneyPrice;
        this.mentorsList = new ArrayList<>(other.mentorsList);
        this.expiredSubscriptionsDate = other.expiredSubscriptionsDate;
        this.judge = other.judge;
        this.infractions = other.infractions;
    }

    public void addMentor(UtenteRegistrato u){
        mentorsList.add(u);
    }

    public boolean removeTeam(Team t){
        teamsList.remove(t);
        return true;
    }
}
