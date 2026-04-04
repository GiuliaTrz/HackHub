package com.project.hackhub.service;

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
import com.project.hackhub.repository.HackathonRepository;
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
     * Invites a {@link UtenteRegistrato} to take part in a {@link Team}.
     *
     * @param user the user to invite
     * @param team the team that extends the invite
     * @throws UserNotAvailableException if the user is not available
     */
    public void inviteUser(UtenteRegistrato user, Team team) {

        if (team == null || user == null)
            throw new IllegalArgumentException("Team or user cannot be null.");

        // Check if hackathon is open for registration and if team leader has permission
        if (!team.getHackathon().getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE)
            || !team.getTeamLeader().hasPermission(Permission.CAN_INVITE_USERS, team.getHackathon()))
            throw new UnsupportedOperationException("Action not allowed.");

        // Check if user is available
        if (user.isAvailable(team.getHackathon().getReservation())) {
            Invito invitation = new Invito(team, user);
            invitoRepo.save(invitation);
            team.addInvitation(invitation);
            teamRepository.save(team);

            EventManager notifier = EventManager.getInstance();
            notifier.notify(EventType.INVITO_UTENTE, List.of(user), invitation);
        } else {
            throw new UserNotAvailableException("User is not available and cannot be invited!");
        }
    }

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
     * Unsubscribes a team from a Hackathon. The team does not exist after his unsubscription
     * @param t the team
     * @param h the hackathon
     * @throws IllegalArgumentException if any of the parameters are null
     * @throws UnsupportedOperationException if the {@link Hackathon} is not in {@link HackathonStateType#IN_ISCRIZIONE} state
     * or the user does not have permission to do the action
     * @author Giorgia Branchesi
     */
    public void unsubscribeTeam(Team t, Hackathon h){

        if(t == null)
            throw new IllegalArgumentException("team cannot be null");
        if(h == null)
            throw new IllegalArgumentException("hackathon cannot be null");

        if(!t.getTeamLeader().hasPermission(Permission.CAN_UNSUBSCRIBE_TEAM, h)
            || !h.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
                throw new UnsupportedOperationException("Cannot unsubscribe team");

        hackathonHandler.removeTeamFromHackathon(h, t);
        EventManager notifier = EventManager.getInstance();
        notifier.notify(EventType.ELIMINAZIONE_TEAM, t.getTeamMembersList(), t);
        teamRepository.delete(t);
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
}
