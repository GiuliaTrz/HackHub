package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.state.Giudice;
import com.project.hackhub.model.utente.state.Organizzatore;
import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity @NoArgsConstructor
public class Report {

    private String name;

    @Id @GeneratedValue
    private UUID id;
    private String description;
    private String ruleBook;
    private LocalDate expiredSubscriptionsDate;
    private int maxTeamDimension;
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
}
