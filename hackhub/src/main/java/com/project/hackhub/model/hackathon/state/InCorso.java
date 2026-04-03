package com.project.hackhub.model.hackathon.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.builder.HackathonReportBuilder;

public class InCorso implements HackathonState {

    @Override
    public HackathonStateType getStateType() {
        return HackathonStateType.IN_CORSO;
    }

    @Override
    public void buildDetailedReport(HackathonReportBuilder builder, Hackathon h) {

    }

    @Override
    public void buildPublicReport(HackathonReportBuilder builder, Hackathon h) {

    }
}
