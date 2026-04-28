package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.state.HackathonStateFactory;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.repository.HackathonRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class SubmissionsTimerHandler {

    private final HackathonRepository hackathonRepository;
    private final HackathonStateFactory hackathonStateFactory;

    /**
     * Closes the possibility to send submissions to a Hackathon changing its state in  {@link HackathonStateType#IN_VALUTAZIONE}
     */
    @Scheduled(fixedRate = 300000) // ogni 5 minuti
    @Transactional
    public void handleExpiredSubmissions() {

        List<Hackathon> expiredHackathons = hackathonRepository.getExpiredSubmissions(LocalDateTime.now());

        for (Hackathon h : expiredHackathons) {
            h.setStateType(HackathonStateType.IN_VALUTAZIONE);
            hackathonRepository.save(h);
        }
    }
}
