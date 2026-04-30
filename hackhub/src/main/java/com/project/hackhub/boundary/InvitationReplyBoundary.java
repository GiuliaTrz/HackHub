package com.project.hackhub.boundary;

import com.project.hackhub.handler.InvitationReplyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for handling invitation responses.
 * Manages acceptance and rejection of team invitations by users.
 */
@RequestMapping("/api/invitation")
@RestController
@RequiredArgsConstructor
public class InvitationReplyBoundary {

    private final InvitationReplyHandler invitationReplyHandler;

    /**
     * Accepts a team invitation.
     *
     * @param user UUID of the authenticated user accepting the invitation
     * @param invitation UUID of the invitation to accept
     * @return confirmation message
     */
    @PostMapping("/{invitation}/accept")
    public ResponseEntity<String> acceptInvitation(
            @AuthenticationPrincipal UUID user,
            @PathVariable UUID invitation) {

        invitationReplyHandler.acceptInvitation(user, invitation);
        return ResponseEntity.ok("Invitation accepted");
    }

    /**
     * Rejects a team invitation.
     *
     * @param user UUID of the authenticated user declining the invitation
     * @param invitation UUID of the invitation to decline
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{invitation}")
    public ResponseEntity<Void> declineInvitation(
            @AuthenticationPrincipal UUID user,
            @PathVariable UUID invitation) {

        invitationReplyHandler.declineInvitation(user, invitation);
        return ResponseEntity.noContent().build();
    }
}
