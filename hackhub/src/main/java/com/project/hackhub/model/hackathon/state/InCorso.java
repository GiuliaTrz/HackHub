package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.ReportData;

public class InCorso implements HackathonState {

    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.IN_CORSO;
    }

    public ReportData getReportData(Hackathon h){
        ReportData r = new ReportData();
        //public data
        r.setName(h.getName());
        r.setRuleBook(h.getRuleBook());
        r.setState(h.getState());
        r.setTeamsList(h.getTeamsList());
        //dettagli
        r.setCoordinator(h.getCoordinator());
        r.setJudge(h.getJudge());
        r.setMentorsList(h.getMentorsList());

        return r;
    }
}
