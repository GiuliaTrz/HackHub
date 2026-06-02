package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import static com.project.hackhub.observer.EventType.PROCLAIM_WINNER;

@Service
public class WinnerChoiceHandler {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public WinnerChoiceHandler(HackathonRepository hackathonRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.hackathonRepository = hackathonRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public Team proclaimWinner(UUID teamId, UUID organizerId, UUID hackathonId) {

        Team t = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon h = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if(!h.getTeamsList().contains(t))
            throw new IllegalArgumentException("Team is not part of the hackathon");
        if(!organizer.hasPermission(Permission.CAN_PROCLAIM_WINNER, h))
            throw new IllegalArgumentException("User does not have required permission");
        if(h.getStateType() != HackathonStateType.APPRAISAL)
            throw new IllegalStateException("Hackathon is not in the right state to proclaim winner");

        h.setWinner(t);
        List<User> usersToUpdate = h.getTeamsList().stream()
                .flatMap(team -> team.getTeamMembersList().stream())
                .toList();
        EventManager.getInstance().notify(PROCLAIM_WINNER, usersToUpdate, "the team winner has been proclaimed!", h);
        setConcluded(h);
        return t;
    }


    /**
     * Helper method that sets the Hackathon state to CONCLUDED and saves changes.
     * @param h the Hackathon of interest
     */
    private void setConcluded(Hackathon h) {
        h.setStateType(HackathonStateType.CONCLUDED);
        this.hackathonRepository.save(h);
    }

    public List<UUID> getAllTeams(UUID organizerId, UUID hackathonId) {

        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon h = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if(!organizer.hasPermission(Permission.CAN_PROCLAIM_WINNER, h))
            throw new IllegalArgumentException("User does not have required permission");
        if(h.getStateType() != HackathonStateType.APPRAISAL)
            throw new IllegalStateException("Hackathon is in the wrong state");

        return h.getTeamsList().stream().map(Team::getId).toList();
    }
}
