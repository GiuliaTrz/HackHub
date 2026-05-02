package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.model.user.state.UserStateType;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UserRepository;
import com.project.hackhub.service.UserStateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TeamHandler {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final UserStateService userStateService;

    /**
     * Creates a new team. The user who creates it becomes the Team Leader.
     * @param creatorId ID of the creator user
     * @param hackathonId ID of the hackathon
     * @param teamName name of the team
     */
    @Transactional
    public void createTeam(UUID creatorId, UUID hackathonId, String teamName) {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("Creator not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!creator.hasPermission(Permission.CAN_CREATE_TEAM, hackathon)) {
            throw new UnsupportedOperationException("User cannot create a team in this hackathon");
        }
        createTeam(teamName, creator, hackathon);
    }

    /**
     * Modifies the name of a team. Only team leader or organizer.
     * @param editorId ID of the user requesting the modification
     * @param teamId ID of the team
     * @param newName new name
     */
    @Transactional
    public void updateTeam(UUID editorId, UUID teamId, String newName) {
        User editor = userRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if(!team.getHackathon().getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE)) {
            throw new UnsupportedOperationException("Operation cannot be performed in this state");
        }

        boolean isLeader = team.getTeamLeader().getId().equals(editorId);
        boolean isOrganizer = editor.hasPermission(Permission.CAN_MANAGE_TEAMS, team.getHackathon());

        if (!isLeader && !isOrganizer) {
            throw new UnsupportedOperationException("Only team leader or organizer can modify the team");
        }

        team.setName(newName);
        teamRepository.save(team);
    }

    /**
     * Removes a member from the team. Only team leader or organizer.
     * @param requesterId ID of the requester
     * @param teamId ID of the team
     * @param memberId ID of the member to remove
     */
    @Transactional
    public void removeMember(UUID requesterId, UUID teamId, UUID memberId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if(!team.getHackathon().getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE)) {
            throw new UnsupportedOperationException("Operation cannot be performed in this state");
        }

        boolean isLeader = team.getTeamLeader().getId().equals(requesterId);
        boolean isOrganizer = requester.hasPermission(Permission.CAN_MANAGE_TEAMS, team.getHackathon());

        if (!isLeader && !isOrganizer) {
            throw new UnsupportedOperationException("Insufficient permissions");
        }
        removeTeamMember(member, team);
    }

    /**
     * Private method for the actual team creation.
     */
    private void createTeam(String name, User leader, Hackathon hackathon) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Team name cannot be null or blank.");
        if (leader == null)
            throw new IllegalArgumentException("Leader cannot be null.");
        if (hackathon == null)
            throw new IllegalArgumentException("Hackathon cannot be null.");

        for(Team t: hackathon.getTeamsList())
            if(t.getName().equals(name))
                throw new IllegalArgumentException("A team with the same name already exists in this hackathon.");

        Team team = new Team(name, hackathon, leader);
        team.addTeamMember(leader);
        userStateService.changeUserState(leader, true, hackathon, UserStateType.TEAM_LEADER);

        teamRepository.save(team);
    }

    /**
     * Private method to remove a team member.
     */
    private void removeTeamMember(User user, Team team) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null.");
        if (team == null)
            throw new IllegalArgumentException("Team cannot be null.");

        if (user.equals(team.getTeamLeader()))
            throw new UnsupportedOperationException("Cannot remove the team leader with this method.");

        if (!team.getTeamMembersList().contains(user))
            throw new IllegalStateException("User is not a member of the team.");

        EventManager.getInstance().notify(EventType.REMOVED_MEMBER_FROM_TEAM, List.of(user), "you have been removed from the team" + team.getId(), team);
        team.removeTeamMember(user);
        teamRepository.save(team);
    }
}