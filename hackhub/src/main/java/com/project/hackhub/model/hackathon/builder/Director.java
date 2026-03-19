package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.dto.HackathonDTO;

public class Director {

    private Builder builder;

    public Director(Builder b){
        this.builder = b;
    }

    public void populateBuilder(HackathonDTO dto){
         builder.setName(dto.name());
         builder.setRuleBook(dto.ruleBook());
         builder.setState(dto.state());
         builder.setJudge(dto.judge());
         builder.setReservation(dto.reservation());
         builder.addMentorsList(dto.mentorsList());
         builder.setExpiredSubscriptionDate(dto.expiredSubscriptionsDate());
         builder.setMoneyPrice(dto.moneyPrice());
         builder.setMaxTeamDimension(dto.maxTeamDimension());
    }

    public void changeBuilder(Builder b){
        this.builder = b;
    }

}
