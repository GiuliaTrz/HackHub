package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.report.Report;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
/**
 * Publicly visible report view with restricted information.
 */
public record PublicReport(String name,
                           String description,
                           String ruleBook,
                           Prenotazione reservation,
                           HackathonState state,
                           Soldi moneyPrize,
                           String teamsGrades) implements Report{}
