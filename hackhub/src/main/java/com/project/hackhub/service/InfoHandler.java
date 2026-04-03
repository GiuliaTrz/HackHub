package com.project.hackhub.service;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Report;
import com.project.hackhub.model.hackathon.builder.Director;
import com.project.hackhub.model.hackathon.builder.HackathonReportBuilder;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.utente.Utente;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InfoHandler {
    private final Director director;
    private final HackathonRepository hackathonRepository;
    //private final UtenteRegistratoRepository utenteRegistratoRepository;

    public InfoHandler(HackathonRepository hr, UtenteRegistratoRepository ur, Director director){
        this.hackathonRepository = hr;
       // this.utenteRegistratoRepository = ur;
        this.director = director;
    }

    /**
     * @return a list of Hakcathons' UUID whose state is not concluded.
     * @Author Chiara Marinucci
     */
    public List<UUID> getActiveHackathon(){
        return hackathonRepository.findIdByStateNot(HackathonStateType.CONCLUSO);
    }

    /**
     * Return a report containing information about a given Hackathon
     * @param hackathonId a unique id associated to a certain Hackathon
     * @param u the user that wants to access the report
     * @return a Report of the Hackathon if the hackathonId is mapped to a Hackathon or null
     * @Author Chiara Marinucci
     */
    public Report getHackathonReport(UUID hackathonId, Utente u){
        Optional<Hackathon> optionalHackathon = hackathonRepository.findById(hackathonId);
        if(optionalHackathon.isPresent()) {
            Hackathon h = optionalHackathon.get();
            if (!u.hasPermission(Permission.DETAILED_INFO, h))
                return getPublicInfo(h);
            else
                return getDetailedInfo(h);
        }
        return null;
    }

    /**
     * Returns a detailed report of a given Hackathon
     * @param h the Hakcahton for which a report must be built
     * @return a Report
     * @Author Chiara Marinucci
     */
    private Report getDetailedInfo(Hackathon h) {
        HackathonReportBuilder builder = new HackathonReportBuilder();
        return director.constructDetailedReport(builder, h);
    }
    /**
     * Returns a general report of a given Hackathon
     * @param h the Hakcahton for which a report must be built
     * @return a Report
     * @Author Chiara Marinucci
     */
    private Report getPublicInfo(Hackathon h) {
        HackathonReportBuilder builder = new HackathonReportBuilder();
        return director.constructPublicReport(builder, h);
    }
}
