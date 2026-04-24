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

    /**
     * Allows user to send a submission associated to a team for a given task
     * @param teamLeader id of the user
     * @param team id of the team
     * @param task if of the task
     * @param fileTemplate the file template containing the submission file
     * @return a ResponseEntity with status CREATED if successful
     * @author Chiara Marinucci
     */
    @PostMapping("/{teamId}/{taskId}")
    public ResponseEntity<Void> sendSubmission(
            @AuthenticationPrincipal UUID teamLeader,
            @PathVariable("teamId") UUID team,
            @PathVariable("taskId") UUID task,
            @RequestBody FileTemplate fileTemplate) {
        submissionHandler.sendSubmission(teamLeader, team, task, fileTemplate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Allows a user to view the list of a team's submission according to user's permissions.
     * Staff can view all teams' submissions, team members only their own.
     * @param user the id of the user
     * @param teamId the id of the team
     * @return a list of the team's submissions if the user has the necessary permission.
     * @author Chiara Marinucci
     */
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Submission>> getTeamSubmissions(
            @AuthenticationPrincipal UUID user,
            @PathVariable UUID teamId){
            List<Submission> submissions = submissionHandler.getTeamSubmissions(user, teamId);
            if(submissions.isEmpty())
                return ResponseEntity.noContent().build(); //204
            return ResponseEntity.ok(submissions); //200 OK
    }
}
