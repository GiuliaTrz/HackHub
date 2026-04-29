package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.report.ReportData;

public class Appraisal implements HackathonState {

    @Override
       public HackathonStateType getStateType() {
        return HackathonStateType.APPRAISAL;
    }

    @Override
    public ReportData getReportData(Hackathon h){
        ReportData r = new ReportData();
        //public data
        r.setName(h.getName());
        r.setRuleBook(h.getRuleBook());
        r.setState(h.getState());
        //dettagli
        r.setCoordinator(h.getCoordinator());
        r.setJudge(h.getJudge());
        r.setMentorsList(h.getMentorsList());
        r.setInfractions(h.getInfractions());

        return r;
    }
}
