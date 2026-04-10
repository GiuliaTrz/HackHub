package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Report;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;

public record PublicReport(String name,
                           String description,
                           String ruleBook,
                           Prenotazione reservation,
                           HackathonState state,
                           Soldi moneyPrize,
                           String teamsGrades) implements Report{}
