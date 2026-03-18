package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.state.Giudice;
import com.project.hackhub.model.utente.state.Organizzatore;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Report {

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
    private final Organizzatore coordinator;
    private final Prenotazione reservation;
}
