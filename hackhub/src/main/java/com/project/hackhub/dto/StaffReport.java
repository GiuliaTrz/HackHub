package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Report;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import java.util.List;

public record StaffReport(String name,
                          String description,
                          String ruleBook,
                          HackathonState state,
                          Soldi moneyPrize,
                          List<Team> teams,
                          List<UtenteRegistrato> mentors,
                          UtenteRegistrato coordinator,
                          UtenteRegistrato judge,
                          Prenotazione reservation,
                          String teamsGrades

) implements Report {}
