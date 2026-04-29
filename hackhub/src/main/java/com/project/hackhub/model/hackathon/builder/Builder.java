package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Reservation;
import com.project.hackhub.model.hackathon.Money;
import com.project.hackhub.model.user.User;
import java.time.LocalDate;
import java.util.List;

public interface Builder {

    void reset();
    void setName(String n);
    void setRuleBook(String r);
    void setState();
    void setMaxTeamDimension(Integer num);
    void setReservation(Reservation p);
    void setMoneyPrice(Money p);
    void addMentorsList(List<User> mentorsList);
    void setExpiredSubscriptionDate(LocalDate d);
    void setJudge(User u);
    void setCoordinator(User coordinator);
}
