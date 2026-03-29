package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.utente.UtenteRegistrato;
import java.time.LocalDate;
import java.util.List;


public record HackathonDTO(

          String name,
          String ruleBook,
          LocalDate expiredSubscriptionsDate,
          int maxTeamDimension,
          List<UtenteRegistrato> mentorsList,
          Soldi moneyPrice,
          UtenteRegistrato judge,
          Prenotazione reservation
) {}
