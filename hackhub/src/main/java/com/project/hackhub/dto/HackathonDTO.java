package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.time.LocalDate;
import java.util.List;


public record HackathonDTO(

          String name,
          String ruleBook,
          LocalDate expiredSubscriptionsDate,
          int maxTeamDimension,
          HackathonState state,
          List<Team>teamsList,
          List<UtenteRegistrato> mentorsList,
          Soldi moneyPrice,
          UtenteRegistrato judge,
          UtenteRegistrato coordinator,
          Prenotazione reservation
) {
}
