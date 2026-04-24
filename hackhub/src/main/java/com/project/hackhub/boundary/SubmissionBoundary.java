package com.project.hackhub.boundary;

import com.project.hackhub.handler.SubmissionHandler;
import com.project.hackhub.model.team.FileTemplate;
import com.project.hackhub.model.team.Submission;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/submission")
public class SubmissionBoundary {
    private final SubmissionHandler submissionHandler;

    public SubmissionBoundary(SubmissionHandler submissionHandler) {
        this.submissionHandler = submissionHandler;
    }

    @PostMapping("/{teamId}/{taskId}")
    public ResponseEntity<Void> sendSubmission(
            @AuthenticationPrincipal UUID teamLeader,
            @PathVariable("teamId") UUID team,
            @PathVariable("taskId") UUID task,
            @RequestBody FileTemplate fileTemplate) {
        submissionHandler.sendSubmission(teamLeader, team, task, fileTemplate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Submission>> getTeamSubmissions(
            @AuthenticationPrincipal UUID user, UUID team,
            @PathVariable String teamId){
            List<Submission> submissions = submissionHandler.getTeamSubmissions(user, team);
            if(submissions.isEmpty())
                return ResponseEntity.noContent().build(); //204
            return ResponseEntity.ok(submissions); //200 OK
    }
}
