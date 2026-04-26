package com.project.hackhub.boundary;

import com.project.hackhub.handler.GradeHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/grades")
public class GradeBoundary {
    private final GradeHandler gradeHandler;

    public GradeBoundary(GradeHandler gradeHandler) {
        this.gradeHandler = gradeHandler;
    }

    /**
     * Allows a judge to grade a submission by providing the submission id and the grade details.
     * The judge must be authenticated to perform this action.
     * @param judge the id of the judge performing the action
     * @param submissionId the id of the submission to grade
     * @param num the record containing the grade
     * @return a ResponseEntity with a success message if the grading is successful
     * @author Chiara Marinucci
     */
    @PatchMapping("/submission/{submissionId}")
    public ResponseEntity<String> gradeSubmission(
            @AuthenticationPrincipal UUID judge,
            @PathVariable UUID submissionId,
            @RequestBody float num){
        gradeHandler.gradeSubmission(judge, submissionId, num);
        return ResponseEntity.ok("submission " + submissionId + " successfully graded");
    }
}
