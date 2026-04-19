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
public class SubscriptionTimerHandler {

    private final HackathonRepository hackathonRepository;
    private final HackathonStateFactory hackathonStateFactory;

    /**
     * Closes the possibility to subscribe to a Hackathon changing its state in  {@link HackathonStateType#IN_CORSO}
     */
    @Scheduled(fixedRate = 300000) // ogni 5 minuti
    @Transactional
    public void handleExpiredSubscriptions() {

        List<Hackathon> expiredHackathons = hackathonRepository.getExpiredSubscriptions(LocalDateTime.now());

        for (Hackathon h : expiredHackathons) {

            HackathonState newState = hackathonStateFactory.createState(HackathonStateType.IN_CORSO);
            h.setState(newState);
            hackathonRepository.save(h);
        }
    }
}
