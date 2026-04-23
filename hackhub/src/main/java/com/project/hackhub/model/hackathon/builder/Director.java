package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.handler.HackathonCreationHandler;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Director class to orchestrate the use of a {@link Builder}
 * @author Giorgia Branchesi
 *
 */
public class Director {

    @NonNull private final Builder builder;
    @NonNull private final HackathonCreationHandler hackathonCreationHandler;
    private final UtenteRegistratoRepository userRepository;

    public Director(@NonNull Builder b, @NonNull HackathonCreationHandler hackathonCreationHandler, UtenteRegistratoRepository userRepository) {
        this.builder = b;
        this.hackathonCreationHandler = hackathonCreationHandler;
        this.userRepository = userRepository;
    }

    /**
     * populates the builder from a dto given
     *
     * @param dto the dto given
     * @throws IllegalArgumentException if the dto is {@code null}
     * @author Giorgia Branchesi
     */
    public void populateBuilder(HackathonDTO dto) {

        if (dto == null) throw new IllegalArgumentException("dto cannot be null");

        builder.reset();

        setBasicInfo(dto);
        setReservation(dto);
        setJudge(dto);
        setMentors(dto);
        setAdditionalInfo(dto);
    }

    private void setBasicInfo(HackathonDTO dto) {
        builder.setName(dto.name());
        builder.setRuleBook(dto.ruleBook());
    }

    private void setReservation(HackathonDTO dto) {
        if (dto.reservation() != null &&
                hackathonCreationHandler.isReservationAvailable(dto.reservation())) {

            builder.setReservation(dto.reservation());
        }
    }

    private void setJudge(HackathonDTO dto) {

        if (dto.judge() != null) {
            UtenteRegistrato j = userRepository.findById(dto.judge()).orElse(null);

            if (dto.reservation() != null && j != null && j.isAvailable(dto.reservation())) {
                builder.setJudge(j);
            }
        }
    }

    private void setMentors(HackathonDTO dto) {

        if (dto.mentorsList() == null || dto.reservation() == null)
            return;

        List<UtenteRegistrato> validMentors = new ArrayList<>();

        for (UUID m : dto.mentorsList()) {
            if(m != null) {
                UtenteRegistrato mentor = userRepository.findById(m).orElse(null);
                if (mentor != null && mentor.isAvailable(dto.reservation())) {
                    validMentors.add(mentor);
                }
            }
        }

        if (!validMentors.isEmpty()) {
            builder.addMentorsList(validMentors);
        }
    }

    private void setAdditionalInfo(HackathonDTO dto) {
        builder.setExpiredSubscriptionDate(dto.expiredSubscriptionsDate());
        builder.setMoneyPrice(dto.moneyPrice());
        builder.setMaxTeamDimension(dto.maxTeamDimension());
    }

}
