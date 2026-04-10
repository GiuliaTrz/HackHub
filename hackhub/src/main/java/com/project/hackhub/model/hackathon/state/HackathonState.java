package com.project.hackhub.model.hackathon.state;


import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.ReportData;
import jakarta.persistence.Embeddable;

@Embeddable
public interface HackathonState {

    HackathonStateType getStateType();

    ReportData getReportData(Hackathon h);

}

