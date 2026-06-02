package com.project.hackhub.boundary;

import com.project.hackhub.dto.GradeDTO;
import com.project.hackhub.handler.GradeHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/evaluation")
public class GradeBoundary {
    private final GradeHandler gradeHandler;

    public GradeBoundary(GradeHandler gradeHandler) {
        this.gradeHandler = gradeHandler;
    }

    @PatchMapping("/submission/{submissionId}")
    public ResponseEntity<String> gradeSubmission(
            @AuthenticationPrincipal UUID judge,
            @PathVariable UUID submissionId,
            @RequestBody GradeDTO evaluation) {
        gradeHandler.gradeSubmission(judge, submissionId, evaluation);
        return ResponseEntity.ok("submission " + submissionId + " successfully graded " + evaluation.grade());
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<String> viewEvaluation(
            @AuthenticationPrincipal UUID teamMemberId,
            @PathVariable UUID teamId) {

        return ResponseEntity.ok(gradeHandler.viewEvaluation(teamMemberId, teamId));
    }
}
