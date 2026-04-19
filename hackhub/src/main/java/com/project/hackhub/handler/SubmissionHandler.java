package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.FileTemplate;
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

import java.util.UUID;

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

    public boolean sendSubmission(UUID teamLeader, UUID team, UUID task, FileTemplate fileTemplate){
        UtenteRegistrato leader = utenteRegistratoRepository.findById(teamLeader).orElseThrow(()-> new IllegalArgumentException("teamLeader can't be null"));
        Team t = teamRepository.findById(team).orElseThrow(()-> new IllegalArgumentException("Team can't be null"));
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


}
