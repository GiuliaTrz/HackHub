package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.builder.HackathonReportBuilder;

public class InIscrizione implements HackathonState {
    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.IN_ISCRIZIONE;
    }

    /**
     * Builds a detailed Report for a given Hackathon
     * @param builder
     * @param h
     * @Author Chiara Marinucci
     */
    @Override
    public void buildDetailedReport(HackathonReportBuilder builder, Hackathon h) {
        buildPublicReport(builder, h);
        builder.setCoordinator(h.getCoordinator());
        builder.setJudge(h.getJudge());
        builder.addMentorsList(h.getMentorsList());
    }

    /**
     * Builds a general Report for a given Hackathon
     * @param builder
     * @param h
     * @Author Chiara Marinucci
     */
    @Override
    public void buildPublicReport(HackathonReportBuilder builder, Hackathon h) {
        builder.setName(h.getName());
        builder.setRuleBook(h.getRuleBook());
        builder.setState(h.getState());
        builder.setReservation(h.getReservation());
        builder.setExpiredSubscriptionDate(h.getExpiredSubscriptionsDate());
        builder.setMoneyPrice(h.getMoneyPrice());
    }
}
