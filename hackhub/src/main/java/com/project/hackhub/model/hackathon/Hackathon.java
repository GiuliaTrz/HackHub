package com.project.hackhub.model.hackathon;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.hackathon.state.HackathonStateFactory;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.model.user.User;
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

    @Column(name = "HackathonName")
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
    private List<User> mentorsList = new ArrayList<>();

    @Embedded
    private Money moneyPrice;

    @ManyToOne
    private User judge;

    @ManyToOne
    private User coordinator;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Reservation reservation;

    @ElementCollection
    private List<Infraction> infractions = new ArrayList<>();

    @ElementCollection
    private List<AidRequest> aidRequests = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> taskList = new ArrayList<>();

    @OneToOne
    private Team winner;


    public void addMentor(User u) {
        if (u == null)
            throw new IllegalArgumentException("Mentor cannot be null.");

        if (mentorsList.contains(u))
            throw new IllegalStateException("Mentor already present.");

        mentorsList.add(u);
    }

    public HackathonState getState() {
        if (stateType == null) {
            return null;
        }
        return factory.createState(stateType);
    }

    /**
     * Removes a mentor
     * @param u registered user
     * @author Giulia Trozzi
     */
    public void removeMentor(User u) {
        if (u == null)
            throw new IllegalArgumentException("Mentor cannot be null.");
        if (!mentorsList.contains(u))
            throw new IllegalStateException("Mentor not present in the list.");
        mentorsList.remove(u);
    }

    public void removeTeam(Team t) {
        if (t == null)
            throw new IllegalArgumentException("Team cannot be null.");

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
            throw new IllegalArgumentException("Team cannot be null");

        for(Infraction i : this.infractions) {
            if(i.getITeam().equals(t)) {
                this.infractions.remove(i);
                return;
            }
        }
    }

    public void addInfraction(Infraction i) {
        if(i == null)
            throw new IllegalArgumentException("Infraction to remove cannot be null");

        this.infractions.add(i);
    }
}