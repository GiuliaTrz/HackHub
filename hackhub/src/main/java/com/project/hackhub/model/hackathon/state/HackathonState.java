package com.project.hackhub.model.hackathon.state;


import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.builder.HackathonReportBuilder;
import jakarta.persistence.Embeddable;

@Embeddable
public interface HackathonState {

    HackathonStateType getStateType();

    void buildDetailedReport(HackathonReportBuilder builder, Hackathon h);

    void buildPublicReport(HackathonReportBuilder builder, Hackathon h);

}

