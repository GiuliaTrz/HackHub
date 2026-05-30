package com.project.hackhub.boundary;

import com.project.hackhub.dto.SubmissionDTO;
import com.project.hackhub.handler.SubmissionHandler;
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

    @PostMapping("/send")
    public ResponseEntity<Void> sendSubmission(
            @AuthenticationPrincipal UUID teamLeader,
            @RequestBody SubmissionDTO dto) {
        submissionHandler.sendSubmission(teamLeader, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Submission>> getAllTeamsSubmissions(
            @AuthenticationPrincipal UUID user,
            @RequestBody UUID hackathonId){
            List<Submission> submissions = submissionHandler.getAllTeamsSubmissions(user, hackathonId);
            if(submissions.isEmpty())
                return ResponseEntity.noContent().build(); //204
            return ResponseEntity.ok(submissions); //200 OK
    }
}
