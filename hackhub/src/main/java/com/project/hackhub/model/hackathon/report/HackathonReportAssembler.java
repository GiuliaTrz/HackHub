package com.project.hackhub.model.hackathon.report;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;

/**
 * A class that builds different report views based on user permissions.
 */

public class HackathonReportAssembler {

    /**
     * Builds a public report containing only publicly visible data.     *
     * @param data aggregated report data
     * @return public report view
     * @author Chiara Marinucci
     */
       public PublicReport buildPublic(ReportData data) {
        return new PublicReport(
                data.getName(),
                data.getRuleBook(),
                data.getReservation(),
                data.getExpiredSubscriptionsDate(),
                data.getState(),
                data.getMoneyPrice(),
                data.getMaxTeamDimension(),
                data.getWinner(),
                data.getTeamsGrades()
        );
    }

    /**
     * Builds a detailed report for Members of a Team subscribed to a certain Hackathon.
     * @param data aggregated report data
     * @return detailed report view
     * @author Chiara Marinucci
     */
    public DetailedReport buildDetailed(ReportData data) {
        return new DetailedReport(
                data.getName(),
                data.getRuleBook(),
                data.getReservation(),
                data.getExpiredSubscriptionsDate(),
                data.getState(),
                data.getMoneyPrice(),
                data.getMaxTeamDimension(),
                data.getWinner(),
                data.getCoordinator(),
                data.getJudge(),
                data.getMentorsList()
        );
    }

    /**
     * Builds a staff report with full administrative data access.
     * @param data aggregated report data
     * @return staff report view
     * @author Chiara Marinucci
     */
    public StaffReport buildStaff(ReportData data) {
        return new StaffReport(
                data.getName(),
                data.getRuleBook(),
                data.getReservation(),
                data.getState(),
                data.getMoneyPrice(),
                data.getMaxTeamDimension(),
                data.getWinner(),
                data.getTeamsList(),
                data.getMentorsList(),
                data.getCoordinator(),
                data.getJudge(),
                data.getTeamsGrades(),
                data.getAidRequests(),
                data.getExpiredSubscriptionsDate(),
                data.getInfractions()
        );
    }

    /**
     * Builds the appropriate report based on user permissions.
     * If user is null or does not have permission TEAM returns a public report.
     * If user has TEAM permission returns a detailed report.
     * If user has STAFF permission returns a report with full administrative data access.
     * @param data aggregated report data
     * @param h hackathon context for permission checks
     * @param u requesting user (nullable)
     * @return report view matching user access level
     * @author Chiara Marinucci
     */
    public Report build(ReportData data, Hackathon h, UtenteRegistrato u) {
        if (u == null)
            return buildPublic(data);
        if (u.hasPermission(Permission.STAFF_PERMISSION, h))
            return buildStaff(data);
        if (u.hasPermission(Permission.TEAM_PERMISSION, h))
            return buildDetailed(data);
        //registered user that is not part of the hackathon
        return buildPublic(data);
    }
}
