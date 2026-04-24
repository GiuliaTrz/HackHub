package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
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
import java.util.*;

@Setter @Getter
@NoArgsConstructor
@Entity
public class Hackathon {

    @Id
    @GeneratedValue
    private UUID id;

    @AttributeOverride(name = "name", column = @Column(name = "hackathonName"))
    private String name;

    private String ruleBook;
    private LocalDate expiredSubscriptionsDate;
    private Integer maxTeamDimension;

    @Embedded
    private HackathonState state;

    @Enumerated(EnumType.STRING)
    private HackathonStateType stateType;

    @OneToMany
    private List<Team> teamsList = new ArrayList<>();

    @OneToMany
    private List<UtenteRegistrato> mentorsList = new ArrayList<>();

    @Embedded
    private Soldi moneyPrice;

    @OneToOne
    private UtenteRegistrato judge;

    @OneToOne
    private UtenteRegistrato coordinator;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Prenotazione reservation;

    @ElementCollection
    private List<Infraction> infractions = new ArrayList<>();

    @ElementCollection
    private List<AidRequest> aidRequests = new ArrayList<>();

    @OneToMany
    private List<Task> taskList = new ArrayList<>();

    @OneToOne
    private Team winner;


    public void addMentor(UtenteRegistrato u) {
        if (u == null)
            throw new IllegalArgumentException("Mentor nullo.");

        if (mentorsList.contains(u))
            throw new IllegalStateException("Mentor già presente.");

        mentorsList.add(u);
    }

    public void setState(HackathonState state) {

        if(state == null)
            throw new IllegalArgumentException("state cannot be null");

        this.state = state;
        this.stateType = state.getStateType();
    }

    /**
     * Rimuove un mentore
     * @param u utente registrato
     * @author Giulia Trozzi
     */
    public void removeMentor(UtenteRegistrato u) {
        if (u == null)
            throw new IllegalArgumentException("Mentor nullo.");
        if (!mentorsList.contains(u))
            throw new IllegalStateException("Mentor non presente nella lista.");
        mentorsList.remove(u);
    }
    public boolean isMentor(UtenteRegistrato u) {
        return mentorsList.contains(u);
    }

    public boolean removeTeam(Team t) {
        if (t == null)
            throw new IllegalArgumentException("Team nullo.");

        return  teamsList.remove(t);
    }

    public void addTeam(Team t) {
        if (t == null)
            throw new IllegalArgumentException("Team nullo.");

        if (teamsList.contains(t))
            throw new IllegalStateException("Team già presente.");

        teamsList.add(t);
    }

    public boolean addAidRequest(@NonNull AidRequest a) {

        return aidRequests.add(a);
    }

    public Map<Team, Float> getTeamsGrades() {
        Map<Team, Float> grades = new HashMap<>();
        for(Team t : this.teamsList){
            grades.put(t, t.getGrade());
        }
        return grades;
    }

    public void addTask(@NonNull Task t){
        taskList.add(t);
    }

    public void removeInfractionByTeam(Team t) {

        if(t == null)
            throw new IllegalArgumentException("team cannot be null");

        for(Infraction i : this.infractions) {
            if(i.getITeam().equals(t)) {
                this.infractions.remove(i);
                return;
            }
        }
    }

    public void addInfraction(Infraction i) {
        if(i == null)
            throw new IllegalArgumentException("infraction to remove cannot be null");

        this.infractions.add(i);
    }
}