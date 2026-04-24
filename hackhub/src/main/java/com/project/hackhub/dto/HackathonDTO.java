package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.utente.UtenteRegistrato;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public record HackathonDTO(

          String name,
          String ruleBook,
          LocalDate expiredSubscriptionsDate,
          Integer maxTeamDimension,
          List<UUID> mentorsList,
          Soldi moneyPrice,
          UUID judge,
          Prenotazione reservation
) {}
