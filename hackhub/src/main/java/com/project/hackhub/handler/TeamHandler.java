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
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.InvitoRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.project.hackhub.service.UserStateService.changeUserState;

@Component
@RequiredArgsConstructor
public class TeamHandler {

    private final UtenteRegistratoRepository userRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final InvitoRepository invitoRepo;

    /**
     * Crea un nuovo team. L'utente che crea diventa Team Leader.
     * @param creatorId ID dell'utente creatore
     * @param hackathonId ID dell'hackathon
     * @param teamName nome del team
     * @return il team creato e salvato
     */
    public Team createTeam(UUID creatorId, UUID hackathonId, String teamName) {
        UtenteRegistrato creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("Creator not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        // Verifica permesso
        if (!creator.hasPermission(Permission.CAN_CREATE_TEAM, hackathon)) {
            throw new UnsupportedOperationException("User cannot create a team in this hackathon");
        }

        // opzionale: verifico che l'utente non sia già in un team per questo hackathon


        return createTeamInternal(teamName, creator, hackathon);
    }

    /**
     * Modifica il nome di un team. Solo team leader o organizzatore.
     * @param editorId ID dell'utente che richiede la modifica
     * @param teamId ID del team
     * @param newName nuovo nome
     * @return team aggiornato
     */
    public Team updateTeam(UUID editorId, UUID teamId, String newName) {
        UtenteRegistrato editor = userRepository.findById(editorId)
                .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        boolean isLeader = team.getTeamLeader().getId().equals(editorId);
        boolean isOrganizer = editor.hasPermission(Permission.CAN_MANAGE_TEAMS, team.getHackathon());

        if (!isLeader && !isOrganizer) {
            throw new UnsupportedOperationException("Only team leader or organizer can modify the team");
        }

        team.setName(newName);
        return teamRepository.save(team);
    }

    /**
     * Rimuove un membro dal team. Solo team leader o organizzatore.
     * @param requesterId ID di chi richiede l'operazione
     * @param teamId ID del team
     * @param memberId ID del membro da rimuovere
     */
    public void removeMember(UUID requesterId, UUID teamId, UUID memberId) {
        UtenteRegistrato requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        UtenteRegistrato member = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        boolean isLeader = team.getTeamLeader().getId().equals(requesterId);
        boolean isOrganizer = requester.hasPermission(Permission.CAN_MANAGE_TEAMS, team.getHackathon());

        if (!isLeader && !isOrganizer) {
            throw new UnsupportedOperationException("Insufficient permissions");
        }
        removeTeamMemberInternal(member, team);
    }

    /**
     * Accetta un invito a un team.
     * @param userId ID dell'utente che accetta (deve corrispondere al destinatario dell'invito)
     * @param invitationId ID dell'invito
     */
    public void acceptInvitation(UUID userId, UUID invitationId) {
        UtenteRegistrato user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Invito invito = invitoRepo.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (!invito.getDestinatario().getId().equals(userId)) {
            throw new UnsupportedOperationException("This invitation is not for the specified user");
        }

        acceptInvitationInternal(invito);
    }

    /**
     * Rifiuta/annulla un invito (rimuove l'invito senza aggiungere al team).
     * @param userId ID dell'utente che rifiuta (mittente o destinatario)
     * @param invitationId ID dell'invito
     */
    public void declineInvitation(UUID userId, UUID invitationId) {
        UtenteRegistrato user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Invito invito = invitoRepo.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        boolean isSender = invito.getMittente().getTeamLeader().getId().equals(userId);
        boolean isReceiver = invito.getDestinatario().getId().equals(userId);

        if (!isSender && !isReceiver) {
            throw new UnsupportedOperationException("User cannot decline this invitation");
        }

        removeInvitationInternal(invito);
    }

    /**
     * Designa un nuovo team leader.
     * @param requesterId ID dell'utente che richiede il cambio (deve avere permessi)
     * @param teamId ID del team
     * @param newLeaderId ID del nuovo leader
     */
    public void chooseNewTeamLeader(UUID requesterId, UUID teamId, UUID newLeaderId) {
        UtenteRegistrato requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        UtenteRegistrato newLeader = userRepository.findById(newLeaderId)
                .orElseThrow(() -> new IllegalArgumentException("New leader not found"));

        boolean isCurrentLeader = team.getTeamLeader().getId().equals(requesterId);
        boolean isOrganizer = requester.hasPermission(Permission.CAN_MANAGE_TEAMS, team.getHackathon());

        if (!isCurrentLeader && !isOrganizer) {
            throw new UnsupportedOperationException("Only current leader or organizer can designate a new leader");
        }

        chooseNewTeamLeaderInternal(newLeader, team);
    }


    /**
     * Metodo interno per la creazione del team (già esistente, adattato).
     */
    private Team createTeamInternal(String name, UtenteRegistrato leader, Hackathon hackathon) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Team name cannot be null or blank.");
        if (leader == null)
            throw new IllegalArgumentException("Leader cannot be null.");
        if (hackathon == null)
            throw new IllegalArgumentException("Hackathon cannot be null.");

        Team team = new Team();
        team.setName(name);
        team.setHackathon(hackathon);
        team.addTeamMember(leader);
        team.setTeamLeader(leader);

        return teamRepository.save(team);
    }

    /**
     * Metodo interno per rimuovere un membro (adattato dal tuo originale).
     */
    private void removeTeamMemberInternal(UtenteRegistrato user, Team team) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null.");
        if (team == null)
            throw new IllegalArgumentException("Team cannot be null.");

        if (user.equals(team.getTeamLeader()))
            throw new UnsupportedOperationException("Cannot remove the team leader with this method.");

        if (!team.getTeamMembersList().contains(user))
            throw new IllegalStateException("User is not a member of the team.");

        changeUserState(user, false, team.getHackathon(), UserStateType.DEFAULT_STATE);
        team.removeTeamMember(user);
        teamRepository.save(team);
    }

    /**
     * Metodo interno per accettare un invito (il tuo originale).
     */
    private void acceptInvitationInternal(Invito i) {
        if (i == null)
            throw new IllegalArgumentException("The invitation cannot be null");

        i.getMittente().addTeamMember(i.getDestinatario());
        removeInvitationInternal(i);
    }

    /**
     * Metodo interno per rimuovere un invito (il tuo originale).
     */
    private void removeInvitationInternal(Invito i) {
        if (i == null)
            throw new IllegalArgumentException("The invitation cannot be null");

        Team t = i.getMittente();
        t.removeInvitationFromList(i);
        teamRepository.save(t);
        invitoRepo.delete(i);
    }

    /**
     * Metodo interno per cambiare team leader (il tuo originale, con piccole correzioni).
     */
    private void chooseNewTeamLeaderInternal(UtenteRegistrato newLeader, Team t) {
        if (newLeader == null)
            throw new IllegalArgumentException("new leader cannot be null");
        if (t == null)
            throw new IllegalArgumentException("team cannot be null");
        if (!newLeader.isAvailable(t.getHackathon().getReservation()))
            throw new IllegalArgumentException("user is not available in said reservation");

        if (!t.getTeamLeader().hasPermission(Permission.CAN_MODIFY_LEADER, t.getHackathon())
                && !t.getHackathon().getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
            throw new UnsupportedOperationException("Cannot choose new Leader");

        UtenteRegistrato oldLeader = t.getTeamLeader();
        t.setTeamLeader(newLeader);
        teamRepository.save(t);
        EventManager.getInstance().notify(EventType.NUOVO_LEADER, List.of(oldLeader, newLeader), t);
    }
}