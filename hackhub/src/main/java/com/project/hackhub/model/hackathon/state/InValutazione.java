package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.builder.HackathonReportBuilder;

public class InValutazione implements HackathonState {

    @Override
       public HackathonStateType getStateType() {
        return HackathonStateType.IN_VALUTAZIONE;
    }

    @Override
    public void buildDetailedReport(HackathonReportBuilder builder, Hackathon h) {

    }

    @Override
    public void buildPublicReport(HackathonReportBuilder builder, Hackathon h) {

    }
}
