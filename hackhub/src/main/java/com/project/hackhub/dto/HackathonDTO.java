package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Money;
import com.project.hackhub.model.hackathon.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public record HackathonDTO(

          String name,
          String ruleBook,
          LocalDate expiredSubscriptionsDate,
          Integer maxTeamDimension,
          List<UUID> mentorsList,
          Money moneyPrize,
          UUID judge,
          Reservation reservation
) {}
