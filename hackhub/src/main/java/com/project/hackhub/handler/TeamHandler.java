package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.InvitoRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.project.hackhub.service.UserStateService.changeUserState;

@Component
@RequiredArgsConstructor
public class TeamHandler {

    private final UtenteRegistratoRepository userRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

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

}