package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.report.ReportData;

public class SubscriptionTime implements HackathonState {
    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.SUBSCRIPTION_PHASE;
    }

    public ReportData getReportData(Hackathon h){
        ReportData r = new ReportData();
        //public data
        r.setName(h.getName());
        r.setRuleBook(h.getRuleBook());
        r.setState(h.getState());
        r.setMoneyPrice(h.getMoneyPrice());
        r.setExpiredSubscriptionsDate(h.getExpiredSubscriptionsDate());
        r.setReservation(h.getReservation());
        r.setMaxTeamDimension(h.getMaxTeamDimension());
        //dettagli
        r.setCoordinator(h.getCoordinator());
        r.setJudge(h.getJudge());
        r.setMentorsList(h.getMentorsList());

        return r;
    }
}
