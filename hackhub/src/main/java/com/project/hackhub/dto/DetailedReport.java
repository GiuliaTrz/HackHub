package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Report;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.utente.UtenteRegistrato;
import java.util.List;

/**
 *Detailed report view intended for users that have TEAM permission.
 */

public record DetailedReport(String name,
                             String description,
                             String ruleBook,
                             HackathonState state,
                             Prenotazione reservation,
                             Soldi moneyPrize,
                             UtenteRegistrato coordinator,
                             UtenteRegistrato judge,
                             List<UtenteRegistrato> mentors
) implements Report {}