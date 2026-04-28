package com.project.hackhub.model.hackathon;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.hackathon.state.HackathonStateFactory;
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

    @Enumerated(EnumType.STRING)
    private HackathonStateType stateType;

    @Transient
    private final HackathonStateFactory factory = new HackathonStateFactory();

    @JsonManagedReference
    @OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teamsList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "hackathon_mentors")
    private List<UtenteRegistrato> mentorsList = new ArrayList<>();

    @Embedded
    private Soldi moneyPrice;

    @ManyToOne
    private UtenteRegistrato judge;

    @ManyToOne
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

    public HackathonState getState() {
        if (stateType == null) {
            return null;
        }
        return factory.createState(stateType);
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

    public void removeTeam(Team t) {
        if (t == null)
            throw new IllegalArgumentException("Team nullo.");

        teamsList.remove(t);
    }

    public void addAidRequest(@NonNull AidRequest a) {

        aidRequests.add(a);
    }

    public Map<String, Float> getTeamsGrades() {
        Map<String, Float> grades = new HashMap<>();
        for(Team t : this.teamsList) {
            grades.put(t.getName(), t.getGrade());
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