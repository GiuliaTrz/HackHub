package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.report.Report;
import com.project.hackhub.model.hackathon.report.ReportData;
import com.project.hackhub.model.hackathon.report.HackathonReportAssembler;
import com.project.hackhub.model.user.User;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class InfoHandler {
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final HackathonReportAssembler reportBuilder = new HackathonReportAssembler();

    public InfoHandler(HackathonRepository hr, UserRepository ur){
        this.hackathonRepository = hr;
        this.userRepository = ur;
    }

    /**
     * @return a list of all Hackathons
     * @author Chiara Marinucci
     */
    @Transactional
    public List<UUID> getAllHackathons() {
        List<Hackathon> list = this.hackathonRepository.findAll();
        List<UUID> res = new ArrayList<>();
        for(Hackathon h : list)
            res.add(h.getId());
        return res;}

    /**
     * Return a report containing information about a given Hackathon according to the state of the Hackathon
     * and the user's permissions.
     * @param hackathonId a unique id associated to a certain Hackathon
     * @param userId a unique id associate to the user that wants to access the report (can be null for visitors)
     * @return a Report of the Hackathon if the hackathonId is mapped to a Hackathon
     * * @throws IllegalArgumentException if hackathonId is not mapped to a Hackathon
     * @author Chiara Marinucci
     */
    @Transactional
    public Report getHackathonReport(UUID hackathonId, UUID userId){
        Hackathon h = hackathonRepository.findById(hackathonId).orElse(null);
        if (h == null)
            throw new IllegalArgumentException("Hackathon given does not exist");

        User u = null;
        if (userId != null) {
            u = userRepository.findById(userId).orElse(null);
        }

        ReportData data = h.getState().getReportData(h);

        return reportBuilder.build(data, h, u);
    }


}
