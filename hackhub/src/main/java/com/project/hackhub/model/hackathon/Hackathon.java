package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter @Getter
@NoArgsConstructor
@Entity
public class Hackathon {

    @Id @GeneratedValue
    private UUID id;

    @AttributeOverride(name = "name", column = @Column(name = "hackathonName"))
    private String name;
    private String ruleBook;
    private LocalDate expiredSubscriptionsDate;
    private int maxTeamDimension;

    @Embedded
    private HackathonState state;

    @OneToMany
    private List<Team> teamsList;

    @OneToMany
    private List<UtenteRegistrato> mentorsList;

    @Embedded
    private Soldi moneyPrice;

    @OneToOne
    private UtenteRegistrato judge;

    @OneToOne
    private UtenteRegistrato coordinator;

    @OneToOne
    private Prenotazione reservation;

    @ElementCollection
    private List<Infraction> infractions;

    @ElementCollection
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

    public boolean addAidRequest(@NonNull AidRequest a){
        return aidRequests.add(a);
    }

}
