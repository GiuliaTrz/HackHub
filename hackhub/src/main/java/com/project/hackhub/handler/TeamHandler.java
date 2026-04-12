package com.project.hackhub.handler;

import com.project.hackhub.exceptions.UserNotAvailableException;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.InvitoRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TeamHandler {

    private final UtenteRegistratoRepository userRepository;
    private final TeamRepository teamRepository;
    private final HackathonHandler hackathonHandler;
    private final UtenteRegistratoHandler userHandler;
    private final InvitoRepository invitoRepo;

    /**
     * Adds a user to the Team and deletes its invitation
     * @param i the invitation
     * @throws IllegalArgumentException if the invitation is null
     * @author Giorgia Branchesi
     */
    public void acceptInvitation(Invito i) {

        if(i == null)
            throw new IllegalArgumentException("the invitation cannot be null");

        i.getMittente().addTeamMember(i.getDestinatario());
        removeInvitation(i);
    }

    /**
     * Deletes the invitation for a user
     *
     * @param i the invitation
     * @throws IllegalArgumentException if the invitation is null
     * @author Giorgia Branchesi
     */
    public void removeInvitation(Invito i) {

        if(i == null)
            throw new IllegalArgumentException("the invitation cannot be null");

        Team t = i.getMittente();
        t.removeInvitationFromList(i);
        teamRepository.save(t);
        invitoRepo.delete(i);
    }

    /**
     * Changes the leader of a team
     * @param newLeader the new leader
     * @param t the {@link Team}
     * @throws IllegalArgumentException if any of the parameters are null
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#IN_ISCRIZIONE} state
     * or the user does not have permission to do the action
     * @author Giorgia Branchesi
     */
    public void chooseNewTeamLeader(UtenteRegistrato newLeader, Team t){

        if(newLeader == null)
            throw new IllegalArgumentException("new leader cannot be null");
        if(t == null)
            throw new IllegalArgumentException("team cannot be null");
        if(!newLeader.isAvailable(t.getHackathon().getReservation()))
            throw new IllegalArgumentException("user is not available in said reservation");

        if(t.getTeamLeader().hasPermission(Permission.CAN_MODIFY_LEADER, t.getHackathon())
                || t.getHackathon().getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
            throw new UnsupportedOperationException("Cannot choose new Leader");

        UtenteRegistrato oldLeader = t.getTeamLeader();
        t.setTeamLeader(newLeader);
        teamRepository.save(t);
        EventManager notifier = EventManager.getInstance();
        notifier.notify(EventType.NUOVO_LEADER, List.of(oldLeader, newLeader), t);
    }

    /**
     * Removes a user from a team
     * @param u the user to remove
     * @param t the team
     * @throws IllegalArgumentException if any of the parameters are null
     * @throws UnsupportedOperationException if the team is composed by only the user
     * @author Giorgia Branchesi
     */
    public void leaveTeam(UtenteRegistrato u, Team t) {

        if(u == null) throw new IllegalArgumentException("user cannot be null");
        if(t == null) throw new IllegalArgumentException("team to leave cannot be null");

        if(t.getTeamMembersList().size() < 2)
            throw new UnsupportedOperationException("cannot leave team! " +
                    "Must change team leader of delete hackathon participation!");

        userHandler.changeUserState(u, false, t.getHackathon(), UserStateType.DEFAULT_STATE);
        t.removeTeamMember(u);
        teamRepository.save(t);
    }

    /**
     * Creates a new {@link Team} in a given {@link Hackathon} with a specified leader.
     * The leader is automatically added to the team's member list and set as the team leader.
     * The team is then persisted in the database.
     *
     * @param name the name of the new team; must not be null or blank
     * @param leader the {@link UtenteRegistrato} who will lead the team; must not be null
     * @param hackathon the {@link Hackathon} in which the team will participate; must not be null
     * @return the newly created {@link Team} instance, persisted in the database
     * @throws IllegalArgumentException if any parameter is null or the team name is blank
     *
     * @author Giulia Trozzi
     */
    public Team createTeam(String name, UtenteRegistrato leader, Hackathon hackathon) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Team name cannot be null or blank.");
        if (leader == null)
            throw new IllegalArgumentException("Leader cannot be null.");
        if (hackathon == null)
            throw new IllegalArgumentException("Hackathon cannot be null.");

        Team team = new Team();
        team.setName(name);
        team.setHackathon(hackathon);
        team.addTeamMember(leader); // aggiunge il leader alla lista membri
        team.setTeamLeader(leader);

        // Salva il team nel DB
        teamRepository.save(team);

        return team;
    }

    /**
     * Removes a member from a {@link Team}.
     * This method cannot remove the team leader; to change or remove the leader, use the appropriate handler method.
     * The user's state is updated via {@link UtenteRegistratoHandler} before removal.
     * The team is then updated in the database.
     *
     * @param user the {@link UtenteRegistrato} to be removed from the team; must not be null
     * @param team the {@link Team} from which the user will be removed; must not be null
     * @throws IllegalArgumentException if either parameter is null
     * @throws UnsupportedOperationException if the specified user is the team leader
     * @throws IllegalStateException if the specified user is not a member of the team
     *
     * @author Giulia Trozzi
     */
    public void removeTeamMember(UtenteRegistrato user, Team team) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null.");
        if (team == null)
            throw new IllegalArgumentException("Team cannot be null.");

        if (user.equals(team.getTeamLeader()))
            throw new UnsupportedOperationException("Cannot remove the team leader with this method.");

        if (!team.getTeamMembersList().contains(user))
            throw new IllegalStateException("User is not a member of the team.");

        // Aggiorna lo stato dell'utente se necessario
        userHandler.changeUserState(user, false, team.getHackathon(), UserStateType.DEFAULT_STATE);

        team.removeTeamMember(user);
        teamRepository.save(team);
    }
}
