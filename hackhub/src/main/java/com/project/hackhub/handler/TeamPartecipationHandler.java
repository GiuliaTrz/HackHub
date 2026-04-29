package com.project.hackhub.handler;

import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import com.project.hackhub.service.UserStateService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@AllArgsConstructor
public class TeamPartecipationHandler {

    private final TeamRepository teamRepository;
    private final UtenteRegistratoRepository userRepository;
    private final UserStateService userStateService;

    /**
     * Removes a user from a team
     * @param user the user to remove
     * @param team the team
     * @throws IllegalArgumentException if any of the parameters or the ids are {@code null}
     * @throws UnsupportedOperationException if the team is composed by only the user
     * @author Giorgia Branchesi
     */
    @Transactional
    public void leaveTeam(UUID user, UUID team) {

        UtenteRegistrato user1 = userRepository.findById(user).orElseThrow(
                () -> new IllegalArgumentException("user cannot be null"));

        Team t = teamRepository.findById(team).orElseThrow(
                () -> new IllegalArgumentException("team to leave cannot be null"));

        if(t.getTeamMembersList().size() < 2)
            throw new UnsupportedOperationException("cannot leave team! " +
                    "Must delete hackathon team participation!");

        if(user1.equals(t.getTeamLeader()))
            throw new UnsupportedOperationException("the team leader cannot leave the team! " +
                    "Must choose a new team leader before leaving the team!");

        userStateService.changeUserState(user1, false, t.getHackathon(), UserStateType.DEFAULT_STATE);
        t.removeTeamMember(user1);
        teamRepository.save(t);
    }
}
