package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.handler.HackathonCreationHandler;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Director class to orchestrate the use of a {@link Builder}
 * @author Giorgia Branchesi
 *
 */
public class Director {

    private final Builder builder;
    private final UtenteRegistratoRepository userRepository;

    public Director(Builder builder, UtenteRegistratoRepository userRepository) {
        this.builder = builder;
        this.userRepository = userRepository;
    }

    public void populateBuilder(HackathonDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("dto cannot be null");
        }

        setBasicInfo(dto);
        setReservation(dto);
        setJudge(dto);
        setMentors(dto);
        setAdditionalInfo(dto);
    }

    private void setBasicInfo(HackathonDTO dto) {
        if (dto.name() != null)
            builder.setName(dto.name());

        if (dto.ruleBook() != null)
            builder.setRuleBook(dto.ruleBook());
    }

    private void setReservation(HackathonDTO dto) {
        if (dto.reservation() != null)
            builder.setReservation(dto.reservation());
    }

    private void setJudge(HackathonDTO dto) {
        if (dto.judge() == null) return;

        userRepository.findById(dto.judge())
                .ifPresent(builder::setJudge);
    }

    private void setMentors(HackathonDTO dto) {

        if (dto.mentorsList() == null) return;

        List<UtenteRegistrato> mentors = dto.mentorsList().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (!mentors.isEmpty()) {
            builder.addMentorsList(mentors);
        }
    }

    private void setAdditionalInfo(HackathonDTO dto) {

        if (dto.expiredSubscriptionsDate() != null)
            builder.setExpiredSubscriptionDate(dto.expiredSubscriptionsDate());

        if (dto.moneyPrice() != null)
            builder.setMoneyPrice(dto.moneyPrice());

        if (dto.maxTeamDimension() != null)
            builder.setMaxTeamDimension(dto.maxTeamDimension());
    }
}