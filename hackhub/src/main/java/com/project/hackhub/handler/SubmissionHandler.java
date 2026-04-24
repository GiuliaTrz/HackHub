package com.project.hackhub.handler;

import com.project.hackhub.model.team.FileTemplate;
import com.project.hackhub.model.hackathon.Task;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Submission;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.SubmissionRepository;
import com.project.hackhub.repository.TaskRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubmissionHandler {
    private final TeamRepository teamRepository;
    private final UtenteRegistratoRepository utenteRegistratoRepository;
    private final SubmissionRepository submissionRepository;
    private final TaskRepository taskRepository;

    public SubmissionHandler(TeamRepository teamRepository, UtenteRegistratoRepository utenteRegistratoRepository, SubmissionRepository submissionRepository, TaskRepository taskRepository) {
        this.teamRepository = teamRepository;
        this.utenteRegistratoRepository = utenteRegistratoRepository;
        this.submissionRepository = submissionRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Processes a task submission from a team leader for a specific hackathon task.
     * @param teamLeader UUID of the user attempting to send the submission.
     * @param team UUID of the team the submission belongs to.
     * @param task UUID of the task being completed.
     * @param fileTemplate The submission content/template to be stored.
     * @return true if the submission was successful, false otherwise.
     * @throws IllegalArgumentException if the leader, team, or task ID does not exist,
     * or if the provided user is not the designated leader of the team.
     * @throws IllegalStateException    if the hackathon is not currently in the "IN_CORSO" state.
     * @author Chiara Marinucci
     */
    @Transactional
    public boolean sendSubmission(UUID teamLeader, UUID team, UUID task, FileTemplate fileTemplate){
        UtenteRegistrato leader = utenteRegistratoRepository.findById(teamLeader).orElseThrow(()-> new IllegalArgumentException("teamLeader not found"));
        Team t = teamRepository.findById(team).orElseThrow(()-> new IllegalArgumentException("Team not found"));
        Task ta = taskRepository.findById(task).orElseThrow(()-> new IllegalArgumentException("Task can't be null"));
        if(!t.getTeamLeader().equals(leader)) throw new IllegalArgumentException("TeamLeader doesn't match the given Team");
        if(!t.getHackathon().getState().getStateType().equals(HackathonStateType.IN_CORSO)) throw new IllegalStateException("Hackathon is not IN_CORSO");
        if(leader.hasPermission(Permission.CAN_SEND_SUBMISSION, t.getHackathon())){
                Submission s = new Submission(t, ta, fileTemplate);
                this.submissionRepository.save(s);
                return true;
        }

        return false;
    }



    /**
     * Returns a list of all most recent submissions sent by a Team according to the user's permissions.
     * Staff can view Submissions before and after they have been evaluated, in Hackathon's states IN_VALUTAZIONE
     * and CONCLUSO.
     * A Team Member will only get access to its own team's submissions once the hackathon's state
     * is "CONCLUSO" to check the given grade for each submission.
     * @param user the user attempting to view the list
     * @param team  the team whose submissions are of interest
     * @return a list of all most recent submissions sent by a Team
     * @author Chiara Marinucci
     */
    public List<Submission> getTeamSubmissions(UUID user, UUID team){
        UtenteRegistrato u = utenteRegistratoRepository.findById(user)
                .orElseThrow(()-> new IllegalArgumentException("staff not found"));
        Team t = teamRepository.findById(team)
                .orElseThrow(()-> new IllegalArgumentException("Team not found"));
        if(u.hasPermission(Permission.STAFF_PERMISSION, t.getHackathon()))
            return getSubmissionsAsStaff(u, t);
        else if(u.hasPermission(Permission.TEAM_PERMISSION, t.getHackathon())
                && t.getTeamMembersList().contains(u))
            return getSubmissionsAsTeamMember(u, t);
        else throw new IllegalArgumentException("user does not have the required permission");
    }
    private List<Submission> getSubmissionsAsStaff(UtenteRegistrato staff, Team t){
        if(t.getHackathon().getState().getStateType() != HackathonStateType.IN_VALUTAZIONE &&
                t.getHackathon().getState().getStateType() != HackathonStateType.CONCLUSO)
            throw new IllegalStateException("Hackathon state is not IN_VALUTAZIONE or CONCLUSO");
        return this.submissionRepository.findLatestSubmissionsByTeamId(t.getId());
    }

    private List<Submission> getSubmissionsAsTeamMember(UtenteRegistrato m, Team t) {
        if(t.getHackathon().getState().getStateType() != HackathonStateType.CONCLUSO)
            throw new IllegalStateException("Hackathon state is not CONCLUSO");
        return this.submissionRepository.findLatestSubmissionsByTeamId(t.getId());
    }

}
