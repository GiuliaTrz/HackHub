package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.dto.DetailedReport;
import com.project.hackhub.dto.PublicReport;
import com.project.hackhub.dto.StaffReport;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Report;
import com.project.hackhub.model.hackathon.ReportData;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;


public class HackathonReportBuilder {


       public PublicReport buildPublic(ReportData data) {
        return new PublicReport(
                data.getName(),
                data.getDescription(),
                data.getRuleBook(),
                data.getState(),
                data.getMoneyPrice()
        );
    }
    public DetailedReport buildDetailed(ReportData data) {
        return new DetailedReport(
                data.getName(),
                data.getDescription(),
                data.getRuleBook(),
                data.getState(),
                data.getMoneyPrice(),
                data.getCoordinator(),
                data.getJudge(),
                data.getMentorsList()
        );
    }
    public StaffReport buildStaff(ReportData data) {
        return new StaffReport(
                data.getName(),
                data.getDescription(),
                data.getRuleBook(),
                data.getState(),
                data.getMoneyPrice(),
                data.getTeamsList(),
                data.getMentorsList(),
                data.getCoordinator(),
                data.getJudge(),
                data.getReservation(),
                data.getTeamsGrades()
        );
    }
    public Report build(ReportData data, Hackathon h, UtenteRegistrato u) {

        if (u == null || !u.hasPermission(Permission.TEAM_PERMISSION, h))
            return buildPublic(data);

        if (u.hasPermission(Permission.STAFF_PERMISSION, h))
            return buildStaff(data);

        return buildDetailed(data);
    }
}
