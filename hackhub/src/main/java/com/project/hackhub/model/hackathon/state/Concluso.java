package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.builder.HackathonReportBuilder;

public class Concluso implements HackathonState {
    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.CONCLUSO;
    }

    @Override
    public void buildDetailedReport(HackathonReportBuilder builder, Hackathon h) {
        buildPublicReport(builder, h);


    }

    @Override
    public void buildPublicReport(HackathonReportBuilder builder, Hackathon h) {

    }
}
