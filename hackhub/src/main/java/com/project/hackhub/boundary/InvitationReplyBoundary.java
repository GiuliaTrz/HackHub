package com.project.hackhub.boundary;

import com.project.hackhub.handler.InvitationReplyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/invitation")
@RestController
@RequiredArgsConstructor
public class InvitationReplyBoundary {

    private final InvitationReplyHandler invitationReplyHandler;

    @PostMapping("/{invitation}/accept")
    public ResponseEntity<String> acceptInvitation(
            @AuthenticationPrincipal UUID user,
            @PathVariable UUID invitation) {

        invitationReplyHandler.acceptInvitation(user, invitation);
        return ResponseEntity.ok("invito accettato");
    }

    @DeleteMapping("/{invitation}")
    public ResponseEntity<Void> declineInvitation(
            @AuthenticationPrincipal UUID user,
            @PathVariable UUID invitation) {

        invitationReplyHandler.declineInvitation(user, invitation);
        return ResponseEntity.noContent().build();
    }
}
