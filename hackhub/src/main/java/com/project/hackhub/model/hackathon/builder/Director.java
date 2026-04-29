package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.exceptions.UserNotAvailableException;
import com.project.hackhub.handler.HackathonCreationHandler;
import com.project.hackhub.model.hackathon.Reservation;
import com.project.hackhub.model.user.User;
import com.project.hackhub.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Director class to orchestrate the use of a {@link Builder}
 * @author Giorgia Branchesi
 *
 */
@AllArgsConstructor
public class Director {

    private final Builder builder;
    private final UserRepository userRepository;
    private final HackathonCreationHandler hackathonCreationHandler;

    public void populateBuilder(HackathonDTO dto, Reservation p) {

        if (dto == null) {
            throw new IllegalArgumentException("dto cannot be null");
        }

        setBasicInfo(dto);
        setReservation(dto);
        setJudge(dto, p);
        setMentors(dto, p);
        setAdditionalInfo(dto);
    }

    private void setBasicInfo(HackathonDTO dto) {
        if (dto.name() != null)
            builder.setName(dto.name());

        if (dto.ruleBook() != null)
            builder.setRuleBook(dto.ruleBook());
    }

    private void setReservation(HackathonDTO dto) {
        if (dto.reservation() != null && hackathonCreationHandler.isReservationAvailable(dto.reservation()))
            builder.setReservation(dto.reservation());
    }

    private void setJudge(HackathonDTO dto, Reservation p) {
        if (dto.judge() == null) return;
        User j = userRepository.findById(dto.judge())
                .orElseThrow(() -> new IllegalArgumentException("Judge not found"));

        if(p != null) {
            if (j.isAvailable(p))
                builder.setJudge(j);
            else
                throw new UserNotAvailableException("Judge is not available for the given reservation");
        }
    }

    private void setMentors(HackathonDTO dto, Reservation p) {

        if (dto.mentorsList() == null) return;

        if(p != null) {
            List<User> mentors = dto.mentorsList().stream()
                    .map(userRepository::findById)
                    .map(opt -> opt.filter(u -> u.isAvailable(p)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();


            if (!mentors.isEmpty()) {
                builder.addMentorsList(mentors);
            }
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