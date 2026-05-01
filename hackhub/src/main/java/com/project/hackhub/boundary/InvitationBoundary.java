package com.project.hackhub.boundary;


import com.project.hackhub.handler.InvitationHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for team invitation operations.
 * Handles user invitations to teams and invitation cancellations.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/invitation")
public class InvitationBoundary {

    private final InvitationHandler invitationHandler;

    /**
     * Sends an invitation to a user to join a team.
     *
     * @param teamLeader UUID of the authenticated team leader sending the invitation
     * @param user UUID of the user to invite
     * @param team UUID of the team
     * @return confirmation message
     */
    @PostMapping("/{team}/invite/{user}")
    public ResponseEntity<String> inviteUser(
            @AuthenticationPrincipal UUID teamLeader,
            @PathVariable UUID user,
            @PathVariable UUID team) {
        invitationHandler.inviteUser(teamLeader, user, team);
        return ResponseEntity.ok("User invited");
    }

    /**
     * Cancels a pending team invitation.
     *
     * @param invitation UUID of the invitation to cancel
     * @param teamMember UUID of the authenticated team member
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/cancel/{invitation}")
    public ResponseEntity<String> cancelInvitation(
            @PathVariable UUID invitation,
            @AuthenticationPrincipal UUID teamMember
    ) {
        invitationHandler.cancelInvitation(invitation, teamMember);
        return ResponseEntity.ok("Invitation deleted");
    }

}