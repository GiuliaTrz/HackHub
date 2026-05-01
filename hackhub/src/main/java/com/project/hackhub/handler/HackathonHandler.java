package com.project.hackhub.handler;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.project.hackhub.observer.EventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HackathonHandler {

    private final HackathonRepository hackathonRepo;
    private final UserRepository utenteRepository;

    /**
     * Deletes a hackathon (only organizer).
     * Before deletion, collects all participants (staff + team members)
     * and passes them to the listener for state reset.
     */
    @Transactional
    public void deleteHackathon(UUID deleterId, UUID hackathonId) {

        User deleter = utenteRepository.findById(deleterId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!deleter.hasPermission(Permission.CAN_DELETE_HACKATHON, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to delete hackathon");
        }

        List<User> participants = new ArrayList<>();

        if (hackathon.getCoordinator() != null) {
            participants.add(hackathon.getCoordinator());
        }
        if (hackathon.getJudge() != null) {
            participants.add(hackathon.getJudge());
        }
        if (hackathon.getMentorsList() != null) {
            participants.addAll(hackathon.getMentorsList());
        }

        if (hackathon.getTeamsList() != null) {
            for (Team team : hackathon.getTeamsList()) {
                if (team.getTeamMembersList() != null) {
                    participants.addAll(team.getTeamMembersList());
                }
            }
        }


        EventManager.getInstance().notify(EventType.HACKATHON_DELETION, participants, hackathon);

        hackathonRepo.delete(hackathon);
    }

    /**
     * Modifies the basic data of a hackathon using a DTO.
     * The reservation is never modified.
     */
    @Transactional
    public Hackathon updateHackathon(UUID editorId, UUID hackathonId, HackathonDTO dto) {
        User editor = utenteRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!editor.hasPermission(Permission.CAN_MODIFY_HACKATHON, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions");
        }

        if(!hackathon.getStateType().equals(HackathonStateType.ONGOING)) {
            throw new UnsupportedOperationException("Operation cannot be performed if the hackathon state is set on: on going");
        }

        if(dto.judge() != null || !dto.mentorsList().isEmpty())
            throw new IllegalArgumentException("Staff roles cannot be modified through this endpoint;" +
                    "Please use the appropriate staff management endpoints.");
      
        hackathon.setName(dto.name());
        hackathon.setRuleBook(dto.ruleBook());
        hackathon.setExpiredSubscriptionsDate(dto.expiredSubscriptionsDate());
        hackathon.setMaxTeamDimension(dto.maxTeamDimension());
        hackathon.setMoneyPrice(dto.moneyPrice());

        List<User> usersToUpdate = new ArrayList<>();
        for(Team t : hackathon.getTeamsList()) {
            usersToUpdate.addAll(t.getTeamMembersList());
        }
        EventManager.getInstance().notify(EventType.MODIFIED_HACKATHON, usersToUpdate, hackathon);

        return hackathonRepo.save(hackathon);
    }
}