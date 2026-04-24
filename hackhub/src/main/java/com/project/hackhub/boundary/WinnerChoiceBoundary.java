package com.project.hackhub.boundary;

import com.project.hackhub.handler.WinnerChoiceHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/winner")
public class WinnerChoiceBoundary {
    private final WinnerChoiceHandler winnerChoiceHandler;

    public WinnerChoiceBoundary(WinnerChoiceHandler winnerChoiceHandler){
        this.winnerChoiceHandler = winnerChoiceHandler;
    }

    /**
     * Trigger the automatic winner choice process for a hackathon.
     * This endpoint is intended to be called by a judge to initiate
     * the selection of the winner team based on highest score recorded.
     * @param judge the id of the judge
     * @param hackathonId the id of the hackathon for which the winner is to be selected
     * @return a response entity with a message confirming the automatic winner selection
     * @author Chiara Marinucci
     */
    @PatchMapping("/{hackathonId}/auto")
    public ResponseEntity<String> chooseWinner(
            @AuthenticationPrincipal UUID judge,
            @PathVariable UUID hackathonId){
        winnerChoiceHandler.chooseWinner(judge, hackathonId);
        return ResponseEntity.ok( "winner team automatically selected for hackathon: " + hackathonId);
    }

    /**
     * Allows a judge to manually select a team as the winner for a hackathon.
     * This endpoint is intended to be used in cases where there are teams tied for highest grade.
     * @param judge the id of the judge
     * @param hackathonId the ide of the hackathon
     * @param teamId the id of the team that the judge wants to select as winner
     * @return a response entity with a message confirming the manual winner selection
     * @author Chiara Marinucci
     */
    @PatchMapping("/{hackathonId}/manual/{teamId}")
    public ResponseEntity<String> chooseWinnerManually(
            @AuthenticationPrincipal UUID judge,
            @PathVariable UUID hackathonId,
            @PathVariable UUID teamId){
        winnerChoiceHandler.chooseWinner(judge, hackathonId, teamId);
        return ResponseEntity.ok("team: "+ teamId+ " successfully selected as winner");
    }

    /**
     * Allows a coordinator to officially proclaim the winner team for a hackathon
     * afetr a judge has selected one. This action triggers hackathon to enter state "CONCLUSO".
     * @param hackathon the id of the hackathon for which to proclaim the winner
     * @param coord the id of the coordinator performing the action
     * @return a response entity confirming the proclamation of the winner team for the specified hackathon
     * @author Chiara Marinucci
     */
    @PatchMapping("/{hackathon}/proclaim")
        public ResponseEntity<String> proclaimWinner(
                @PathVariable UUID hackathon,
                @AuthenticationPrincipal UUID coord){
        winnerChoiceHandler.proclaimWinner(hackathon, coord);
        return ResponseEntity.ok("winner team proclaimed for hackathon: " + hackathon);
    }

}
