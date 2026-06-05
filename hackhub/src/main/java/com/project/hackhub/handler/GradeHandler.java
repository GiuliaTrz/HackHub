package com.project.hackhub.handler;


import com.project.hackhub.dto.GradeDTO;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Submission;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.repository.SubmissionRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class GradeHandler {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public GradeHandler(SubmissionRepository submissionRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Grades a specific submission after validating judge permissions and Hackathon state.     *
     * @param judge UUID of the user performing the evaluation.
     * @param submissionId UUID of the submission to be graded.
     * @throws IllegalArgumentException if entities are not found or permissions are missing.
     * @throws IllegalStateException if the Hackathon is not in the evaluation phase.
     * @author Chiara Marinucci
     */
    @Transactional
    public void gradeSubmission(UUID judge, UUID submissionId, GradeDTO evaluation) {
        if(evaluation.grade() < 0 || evaluation.grade() > 10)
            throw new IllegalArgumentException("Grade not valid; must be between 0 and 10");
        if(evaluation.writtenEvaluation() == null)
            throw new IllegalArgumentException("Written evaluation cannot be null");
        User j = userRepository.findById(judge)
                .orElseThrow(()-> new IllegalArgumentException("judge not found"));
        Submission s = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));

        Team t = s.getTeam();
        if(!t.getHackathon().getState().getStateType().equals(HackathonStateType.APPRAISAL))
             throw new IllegalStateException("Hackathon is not in APPRAISAL state");
        if(!j.hasPermission(Permission.CAN_GRADE_SUBMISSION, t.getHackathon()))
            throw new IllegalArgumentException("User does not have required permission");
        float num = evaluation.grade();
        s.setGrade(num);
        s.setWrittenEvaluation(evaluation.writtenEvaluation());
        if(t.getGrade() + num < 0.0)
          t.setGrade((float) 0.0);
        else t.setGrade(num);
        t.setWrittenEvaluation(evaluation.writtenEvaluation());
        submissionRepository.save(s);
        }

    @Transactional
    public String viewEvaluation(UUID teamMemberId, UUID teamId) {

        Team team = teamRepository.findById(teamId)
                   .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        User teamMember = userRepository.findById(teamMemberId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            if(!team.getTeamMembersList().contains(teamMember))
                throw new IllegalArgumentException("User is not a member of the team");
            if(team.getHackathon().getStateType() != HackathonStateType.CONCLUDED)
                throw new IllegalArgumentException("The team evaluation can only be viewed after the hackathon is concluded");

            return "your team grade is " + team.getGrade() + " and the written evaluation is: " + team.getWrittenEvaluation();
        }
}