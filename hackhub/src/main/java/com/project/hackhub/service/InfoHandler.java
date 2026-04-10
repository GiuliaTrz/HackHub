package com.project.hackhub.service;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Report;
import com.project.hackhub.model.hackathon.ReportData;
import com.project.hackhub.model.hackathon.builder.HackathonReportBuilder;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class InfoHandler {
    private final HackathonRepository hackathonRepository;
    private final UtenteRegistratoRepository utenteRegistratoRepository;
    private final HackathonReportBuilder reportBuilder = new HackathonReportBuilder();

    public InfoHandler(HackathonRepository hr, UtenteRegistratoRepository ur){
        this.hackathonRepository = hr;
        this.utenteRegistratoRepository = ur;
    }

    /**
     * @return a list of all Hakcathons
     * @author Chiara Marinucci
     */

    public List<Hackathon> getAllHackathon() {return hackathonRepository.findAll();}

    /**
     * Return a report containing information about a given Hackathon according to the state of the Hackathon
     * and the user's permissions.
     * @param hackathonId a unique id associated to a certain Hackathon
     * @param utenteId a unique id associate to the user that wants to access the report
     * @return a Report of the Hackathon if the hackathonId is mapped to a Hackathon
     * * @throws NoSuchElementException if hackathonId is not mapped to a Hackathon
     * @author Chiara Marinucci
     */
    public Report getHackathonReport(UUID hackathonId, UUID utenteId){
        Hackathon h = hackathonRepository.findById(hackathonId).orElse(null);
        if (h == null)
            throw new NoSuchElementException("Hackathon given does not exist");

        UtenteRegistrato u = utenteRegistratoRepository.findById(utenteId).orElse(null);

        ReportData data = h.getState().getReportData(h);

        return reportBuilder.build(data, h, u);
    }


}
