package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.service.HackathonHandler;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Director class to orchestrate the use of a {@link Builder}
 * @author Giorgia Branchesi
 *
 */
public class Director {

    @NonNull private Builder builder;
    @NonNull private final HackathonHandler hackathonHandler;

    public Director(@NonNull Builder b, @NonNull HackathonHandler hackathonHandler) {
        this.builder = b;
        this.hackathonHandler = hackathonHandler;
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
                hackathonHandler.isReservationAvailable(dto)) {

            builder.setReservation(dto.reservation());
        }
    }

    private void setJudge(HackathonDTO dto) {
        if (dto.judge() != null &&
                dto.reservation() != null &&
                dto.judge().isAvailable(dto.reservation())) {

            builder.setJudge(dto.judge());
        }
    }

    private void setMentors(HackathonDTO dto) {

        if (dto.mentorsList() == null || dto.reservation() == null)
            return;

        List<UtenteRegistrato> validMentors = new ArrayList<>();

        for (UtenteRegistrato mentor : dto.mentorsList()) {
            if (mentor != null && mentor.isAvailable(dto.reservation())) {
                validMentors.add(mentor);
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

    public void changeBuilder(@NonNull Builder b) {
        this.builder = b;
    }

}
