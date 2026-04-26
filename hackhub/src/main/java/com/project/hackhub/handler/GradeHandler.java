package com.project.hackhub.handler;


import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Submission;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.SubmissionRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GradeHandler {

    private final SubmissionRepository submissionRepository;
    private final UtenteRegistratoRepository utenteRegistratoRepository;
    private final TeamRepository teamRepository;

    public GradeHandler(SubmissionRepository submissionRepository, UtenteRegistratoRepository utenteRegistratoRepository,
                        TeamRepository teamRepository) {
        this.submissionRepository = submissionRepository;
        this.utenteRegistratoRepository = utenteRegistratoRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Grades a specific submission after validating judge permissions and Hackathon state.     *
     * @param judge UUID of the user performing the evaluation.
     * @param submissionId UUID of the submission to be graded.
     * @param num the grade to assign to the submission.
     * @throws IllegalArgumentException if entities are not found or permissions are missing.
     * @throws IllegalStateException if the Hackathon is not in the evaluation phase.
     * @author Chiara Marinucci
     */
    public void gradeSubmission(UUID judge, UUID submissionId, float num) {
        UtenteRegistrato j = utenteRegistratoRepository.findById(judge)
                .orElseThrow(()-> new IllegalArgumentException("judge not found"));
        Submission s = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
        Team t = s.getTeam();
        if(!t.getHackathon().getState().getStateType().equals(HackathonStateType.IN_VALUTAZIONE))
            throw new IllegalStateException("Hackathon is not IN_VALUTAZIONE");
        if(!j.hasPermission(Permission.CAN_GRADE_SUBMISSION, t.getHackathon()))
            throw new IllegalArgumentException("User does not have required permission");
        s.setGrade(num+t.getGrade());
        submissionRepository.save(s);
        updateTeamFinalGrade(t);
        }

    /**
     * Calculates and updates the team's final grade if all submissions are evaluated.
     *
     * @param t The Team to update.
     */
    private void updateTeamFinalGrade(Team t){
            List<Submission> allSubmissions = submissionRepository.findLatestSubmissionsByTeamId(t.getId());
            boolean isEverythingGraded = allSubmissions.stream()
                    .allMatch(sub -> sub.getGrade() != null);
            if(isEverythingGraded && !allSubmissions.isEmpty()){
                float sum = 0;
                for (Submission sub : allSubmissions)
                    sum += sub.getGrade();

                float average = sum / allSubmissions.size();
                t.setGrade(average);
                teamRepository.save(t);
        }
    }
}