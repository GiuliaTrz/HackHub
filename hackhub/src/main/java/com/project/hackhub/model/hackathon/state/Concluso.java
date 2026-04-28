package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.report.ReportData;

public class Concluso implements HackathonState {

    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.CONCLUSO;
    }

    @Override
    public ReportData getReportData(Hackathon h){
        ReportData r = new ReportData();
        //public data
        r.setName(h.getName());
        r.setRuleBook(h.getRuleBook());
        r.setState(h.getState());
        r.setTeamsGrades(h.getTeamsGrades());
        //dettagli
        r.setCoordinator(h.getCoordinator());
        r.setJudge(h.getJudge());
        r.setMentorsList(h.getMentorsList());
        r.setWinner(h.getWinner());

        return r;
    }
}
