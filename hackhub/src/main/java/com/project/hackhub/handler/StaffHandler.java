package com.project.hackhub.handler;

import com.project.hackhub.exceptions.UserNotAvailableException;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.model.user.state.UserStateType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UserRepository;
import com.project.hackhub.service.UserStateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StaffHandler {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final UserStateService userStateService;

    /**
     * Adds a user as a mentor to the hackathon.
     * @param organizerId ID of the organizer performing the action
     * @param hackathonId ID of the hackathon
     * @param mentorId ID of the user to add as mentor
     * @throws UnsupportedOperationException if the organizer doesn't have permissions
     * @throws IllegalArgumentException if one of the IDs does not correspond to an entity
     * @author Giulia Trozzi
     */
    @Transactional
    public void addMentor(UUID organizerId, UUID hackathonId, UUID mentorId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));

        if (!organizer.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to manage staff");
        }
        if(!hackathon.getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE))
            throw new IllegalStateException("Hackathon must be in subscription phase to add mentors");

        if (!mentor.isAvailable(hackathon.getReservation())) {
            throw new IllegalStateException("Mentor is not available for this hackathon");
        }

        hackathon.addMentor(mentor);
        userStateService.changeUserState(mentor, true, hackathon, UserStateType.MENTOR);
        hackathonRepository.save(hackathon);
        userRepository.save(mentor);
    }

    /**
     * Removes a mentor from the hackathon (assigns DEFAULT state).
     * @param organizerId ID organizer
     * @param hackathonId ID hackathon
     * @param mentorId ID mentor to remove
     * @author Giulia Trozzi
     */
    @Transactional
    public void removeMentor(UUID organizerId, UUID hackathonId, UUID mentorId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));

        if (!organizer.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to manage staff");
        }

        if (!hackathon.getMentorsList().contains(mentor)) {
            throw new IllegalArgumentException("User is not a mentor of this hackathon");
        }
        if(hackathon.getMentorsList().size() == 1)
            throw new IllegalStateException("Cannot remove the only mentor");

        hackathon.removeMentor(mentor);
        userStateService.changeUserState(mentor, false, hackathon, UserStateType.DEFAULT_STATE);
        hackathonRepository.save(hackathon);
        userRepository.save(mentor);
    }

    /**
     * Modifies the role of a user within a hackathon.
     * Can only be executed by an organizer with staff management permissions.
     * The organizer cannot change their own role nor leave the hackathon without organizers.
     *
     * @param organizerId  ID of the organizer performing the operation
     * @param hackathonId  ID of the hackathon
     * @param targetUserId ID of the user whose role is to be modified
     * @param newRole      new role to assign ("ORGANIZER", "MENTOR", "JUDGE")
     * @throws IllegalArgumentException      if one of the IDs does not correspond to an entity or if the role is not valid
     * @throws UnsupportedOperationException if the organizer does not have the necessary permissions,
     *                                       if they try to modify their own role,
     *                                       or if the operation would leave the hackathon without organizers
     * @throws IllegalStateException         if the target user is not available (already has an incompatible state)
     */
    @Transactional
    public void changeStaffRole(UUID organizerId, UUID hackathonId, UUID targetUserId, String newRole) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Target user not found"));

        if(!hackathon.getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE)) {
            throw new UnsupportedOperationException("Operation cannot be performed if this state");
        }

        if (!organizer.hasPermission(Permission.CAN_MANAGE_STAFF, hackathon)) {
            throw new UnsupportedOperationException("Insufficient permissions to manage staff");
        }

        UserStateType targetState = parseRole(newRole);

        if (organizer.getId().equals(targetUserId)) {
            throw new UnsupportedOperationException("An organizer cannot change their own role");
        }

        if (!targetUser.isAvailable(hackathon.getReservation())) {
            if(targetUser.equals(hackathon.getJudge()) || hackathon.getMentorsList().size() == 1)
            throw new UserNotAvailableException("Must have at least a judge and a mentor; please switch with another user.");
            else
                throw new UserNotAvailableException("User not available as staff");
            }

        assignRoleToUser(targetUser, hackathon, targetState);
        userStateService.changeUserState(targetUser, true, hackathon, targetState);
        hackathonRepository.save(hackathon);
        userRepository.save(targetUser);
    }

    private UserStateType parseRole(String role) {
        return switch (role.toUpperCase()) {
            case "COORDINATOR" -> UserStateType.COORDINATOR;
            case "MENTOR" -> UserStateType.MENTOR;
            case "JUDGE" -> UserStateType.JUDGE;
            default -> throw new IllegalArgumentException("Invalid role. Use COORDINATOR, MENTOR, or JUDGE");
        };
    }


    private void assignRoleToUser(User user, Hackathon hackathon, UserStateType role) {
        switch (role) {
            case COORDINATOR -> {
                if (hackathon.getCoordinator() != null) {
                    throw new IllegalStateException("Hackathon already has a coordinator. Current model supports only one organizer.");
                }
                hackathon.setCoordinator(user);
            }
            case MENTOR -> hackathon.addMentor(user);
            case JUDGE -> {
                if (hackathon.getJudge() != null) {
                    User oldJudge = hackathon.getJudge();
                    hackathon.setJudge(null);
                    userStateService.changeUserState(oldJudge, false, hackathon, UserStateType.DEFAULT_STATE);
                }
                hackathon.setJudge(user);
            }
            default -> throw new IllegalArgumentException("Unhandled role: " + role);
        }
    }
}