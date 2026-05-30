package com.project.hackhub.handler;

import com.project.hackhub.dto.SubmissionDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.FileTemplate;
import com.project.hackhub.model.team.Submission;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.SubmissionRepository;
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
    private final HackathonRepository hackathonRepository;

    public SubmissionHandler(TeamRepository teamRepository, UserRepository userRepository, SubmissionRepository submissionRepository, HackathonRepository hackathonRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.hackathonRepository = hackathonRepository;
    }

    @Transactional
    public void sendSubmission(UUID teamLeader, SubmissionDTO dto){

        User leader = userRepository.findById(teamLeader).orElseThrow(()-> new IllegalArgumentException("teamLeader not found"));
        Team t = teamRepository.findById(dto.teamId()).orElseThrow(()-> new IllegalArgumentException("Team not found"));

        if(!t.getTeamLeader().equals(leader)) throw new IllegalArgumentException("TeamLeader doesn't match the given Team");
        if(!t.getHackathon().getState().getStateType().equals(HackathonStateType.ONGOING)) throw new IllegalStateException("Hackathon is not in ONGOING state");
        if(leader.hasPermission(Permission.CAN_SEND_SUBMISSION, t.getHackathon())){
            FileTemplate ft = new FileTemplate();
            ft.setFileName(dto.fileName());
                Submission s = new Submission(t, ft);
                s.setHackathon(t.getHackathon());
                this.submissionRepository.save(s);
        }

    }



    @Transactional
    public List<Submission> getAllTeamsSubmissions(UUID user, UUID hackathon){
        User u = userRepository.findById(user)
                .orElseThrow(()-> new IllegalArgumentException("staff not found"));
        Hackathon h = hackathonRepository.findById(hackathon)
                .orElseThrow(()-> new IllegalArgumentException("Hackathon not found"));
        if(h.getState().getStateType() != HackathonStateType.APPRAISAL &&
                h.getState().getStateType() != HackathonStateType.CONCLUDED)
            throw new IllegalStateException("Hackathon state is not APPRAISAL or CONCLUDED");
        if(u.hasPermission(Permission.STAFF_PERMISSION, h))
            return this.submissionRepository.findLatestSubmissionsByHackathon(h);
        else throw new IllegalArgumentException("user does not have the required permission");
    }


}
