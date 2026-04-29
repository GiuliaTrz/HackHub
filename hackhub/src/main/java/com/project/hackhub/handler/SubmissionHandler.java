package com.project.hackhub.handler;

import com.project.hackhub.dto.SubmissionDTO;
import com.project.hackhub.model.team.FileTemplate;
import com.project.hackhub.model.hackathon.Task;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Submission;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.repository.SubmissionRepository;
import com.project.hackhub.repository.TaskRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubmissionHandler {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final TaskRepository taskRepository;

    public SubmissionHandler(TeamRepository teamRepository, UserRepository userRepository, SubmissionRepository submissionRepository, TaskRepository taskRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Processes a task submission from a team leader for a specific hackathon task.
     *
     * @param teamLeader   UUID of the user attempting to send the submission.
     * @param dto          Data transfer object containing the submission details,
     *                    including team ID, task ID, and file information.
     * @throws IllegalArgumentException if the leader, team, or task ID does not exist,
     * or if the provided user is not the designated leader of the team.
     * @throws IllegalStateException    if the hackathon is not currently in the "IN_CORSO" state.
     * @author Chiara Marinucci
     */
    @Transactional
    public void sendSubmission(UUID teamLeader, SubmissionDTO dto){
        User leader = userRepository.findById(teamLeader).orElseThrow(()-> new IllegalArgumentException("teamLeader not found"));
        Team t = teamRepository.findById(dto.teamId()).orElseThrow(()-> new IllegalArgumentException("Team not found"));
        Task ta = taskRepository.findById(dto.taskId()).orElseThrow(()-> new IllegalArgumentException("Task can't be null"));
        if(!t.getTeamLeader().equals(leader)) throw new IllegalArgumentException("TeamLeader doesn't match the given Team");
        if(!t.getHackathon().getState().getStateType().equals(HackathonStateType.ONGOING)) throw new IllegalStateException("Hackathon is not IN_CORSO");
        if(leader.hasPermission(Permission.CAN_SEND_SUBMISSION, t.getHackathon())){
            FileTemplate ft = new FileTemplate();
            ft.setFileName(dto.fileName());
                Submission s = new Submission(t, ta, ft);
                this.submissionRepository.save(s);
        }

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
    @Transactional
    public List<Submission> getTeamSubmissions(UUID user, UUID team){
        User u = userRepository.findById(user)
                .orElseThrow(()-> new IllegalArgumentException("staff not found"));
        Team t = teamRepository.findById(team)
                .orElseThrow(()-> new IllegalArgumentException("Team not found"));
        if(u.hasPermission(Permission.STAFF_PERMISSION, t.getHackathon()))
            return getSubmissionsAsStaff(t);
        else if(u.hasPermission(Permission.TEAM_PERMISSION, t.getHackathon())
                && t.getTeamMembersList().contains(u))
            return getSubmissionsAsTeamMember(t);
        else throw new IllegalArgumentException("user does not have the required permission");
    }
    private List<Submission> getSubmissionsAsStaff(Team t){
        if(t.getHackathon().getState().getStateType() != HackathonStateType.APPRAISAL &&
                t.getHackathon().getState().getStateType() != HackathonStateType.CONCLUDED)
            throw new IllegalStateException("Hackathon state is not IN_VALUTAZIONE or CONCLUSO");
        return this.submissionRepository.findLatestSubmissionsByTeamId(t.getId());
    }

    private List<Submission> getSubmissionsAsTeamMember(Team t) {
        if(t.getHackathon().getState().getStateType() != HackathonStateType.CONCLUDED)
            throw new IllegalStateException("Hackathon state is not CONCLUSO");
        return this.submissionRepository.findLatestSubmissionsByTeamId(t.getId());
    }

}
