package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
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

    /**
     * Closes the possibility to subscribe to a Hackathon changing its state in  {@link HackathonStateType#ONGOING}
     */
    @Scheduled(fixedRate = 300000) // ogni 5 minuti
    @Transactional
    public void handleExpiredSubscriptions() {

        List<Hackathon> expiredHackathons = hackathonRepository.getExpiredSubscriptions(LocalDateTime.now());

        for (Hackathon h : expiredHackathons) {
            h.setStateType(HackathonStateType.ONGOING);
            hackathonRepository.save(h);
        }
    }
}
