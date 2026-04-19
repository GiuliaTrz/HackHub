package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InfractionHandler {

    private final HackathonRepository hackathonRepository;
    private final UtenteRegistratoRepository userRepository;
    private final TeamRepository teamRepository;

    /**
     * Elimina una segnalazione di illecito.
     * Poiché Infraction è un @Embeddable senza ID, la identifico tramite hackathonId e indice.
     *
     * @param deleterId       ID dell'utente (organizzatore o mentore)
     * @param hackathonId     ID dell'hackathon
     * @param infractionIndex posizione della segnalazione nella lista
     * @author Giulia Trozzi
     */
    public void deleteInfraction(UUID deleterId, UUID hackathonId, int infractionIndex) {
        UtenteRegistrato deleter = userRepository.findById(deleterId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        boolean isOrganizer = deleter.equals(hackathon.getCoordinator());
        boolean isMentor = hackathon.getMentorsList().contains(deleter);
        if (!isOrganizer && !isMentor) {
            throw new UnsupportedOperationException("Solo organizzatore o mentore possono eliminare una segnalazione");
        }

        if (infractionIndex < 0 || infractionIndex >= hackathon.getInfractions().size()) {
            throw new IllegalArgumentException("Indice segnalazione non valido");
        }

        hackathon.getInfractions().remove(infractionIndex);
        hackathonRepository.save(hackathon);
    }

    /**
     * Expels a team from a Hackathon
     * @param team the team to expel
     * @param coordinator the coordinator of the Hackathon
     * @throws IllegalArgumentException if the parameters do not exist in their repository
     * or if the state of the Hackathon is not {@link HackathonStateType#IN_CORSO} o {@link HackathonStateType#IN_VALUTAZIONE}
     * or if the coordinator does not have the permission to expel the team
     * @author Giorgia Branchesi
     */
    public void expelTeam(UUID team, UUID coordinator) {

        UtenteRegistrato coord = userRepository.findById(coordinator).orElseThrow(() -> new IllegalArgumentException("coordinator cannot be null"));
        Team t = teamRepository.findById(team).orElseThrow(() -> new IllegalArgumentException("team to expel cannot be null"));
        Hackathon h = t.getHackathon();

        if(!coord.hasPermission(Permission.CAN_EXPEL_TEAM, h)
            && !(h.getStateType().equals(HackathonStateType.IN_CORSO) ||
                h.getStateType().equals(HackathonStateType.IN_VALUTAZIONE)))
            throw new UnsupportedOperationException("cannot perform this action");

        h.removeTeam(t);
        hackathonRepository.save(h);
        List<UtenteRegistrato> teamMembers = t.getTeamMembersList();
        EventManager.getInstance().notify(EventType.ESPULSIONE_TEAM, teamMembers, h);
        teamRepository.delete(t);
    }
}