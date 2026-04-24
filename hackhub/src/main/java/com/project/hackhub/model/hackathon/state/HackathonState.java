package com.project.hackhub.model.hackathon.state;


import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.report.ReportData;

public interface HackathonState {

    HackathonStateType getStateType();
    ReportData getReportData(Hackathon h);

}

