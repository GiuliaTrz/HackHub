package com.project.hackhub.boundary;


import com.project.hackhub.handler.InvitationHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/invitation")
public class InvitationBoundary {

    private final InvitationHandler invitationHandler;

    @PostMapping("{team}/invite/{user}")
    public ResponseEntity<Void> inviteUser(
            @AuthenticationPrincipal UUID teamLeader,
            @PathVariable UUID user,
            @PathVariable UUID team) {
        invitationHandler.inviteUser(teamLeader, user, team);
        return ResponseEntity.ok().build();
    }

}
