package com.project.hackhub.handler;

import com.project.hackhub.dto.InfractionDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.project.hackhub.observer.EventType.ILLECITO;
import static com.project.hackhub.observer.EventType.PENALIZZAZIONE_TEAM;

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
    @Transactional
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
    @Transactional
    public void expelTeam(UUID team, UUID coordinator) {

        UtenteRegistrato coord = userRepository.findById(coordinator).orElseThrow(() -> new IllegalArgumentException("coordinator cannot be null"));
        Team t = teamRepository.findById(team).orElseThrow(() -> new IllegalArgumentException("team to expel cannot be null"));
        Hackathon h = t.getHackathon();

        if(!coord.hasPermission(Permission.CAN_EXPEL_TEAM, h)
           || !(h.getStateType().equals(HackathonStateType.IN_CORSO) ||
                !h.getStateType().equals(HackathonStateType.IN_VALUTAZIONE)))
            throw new UnsupportedOperationException("cannot perform this action");

        h.removeTeam(t);
        h.removeInfractionByTeam(t);
        hackathonRepository.save(h);
        List<UtenteRegistrato> teamMembers = t.getTeamMembersList();
        EventManager.getInstance().notify(EventType.ESPULSIONE_TEAM, teamMembers, h);
        teamRepository.delete(t);
    }

    /**
     * Lets a coordinator handle an infraction
     * @param coordinator the coordinator
     * @param team the team that committed the infraciton
     * @throws IllegalArgumentException if any of the parameters are null, if the coordinator
     * does not have the permission, if it does not exist an infraction committed
     * by that team or if the Hackathon is not in {@link HackathonStateType#IN_CORSO} o {@link HackathonStateType#IN_VALUTAZIONE}
     */
    @Transactional
    public void handleInfraction(UUID coordinator, UUID team) {

        UtenteRegistrato coord = userRepository.findById(coordinator).orElseThrow(
                () -> new IllegalArgumentException("coordinator cannot be null"));
        Team t = teamRepository.findById(team).orElseThrow(
                () -> new IllegalArgumentException("team to expel cannot be null"));
        Hackathon h = t.getHackathon();
        Infraction i = hackathonRepository.findInfractionByTeam(h, t).orElseThrow(
                () -> new IllegalArgumentException("infraction does not exist"));

        if (!coord.hasPermission(Permission.CAN_MANAGE_INFRACTIONS, h)
                || !(h.getStateType().equals(HackathonStateType.IN_CORSO) ||
                !h.getStateType().equals(HackathonStateType.IN_VALUTAZIONE)))
            throw new UnsupportedOperationException("cannot perform this action");

        //MESSAGGIO AD API "ESPELLI O PENALIZZA TEAM"
    }

    /**
     * Lets a coordinator penalize a team
     * @param coordinator the coordinator
     * @param team the team to penalize
     * @param points point to remove from the team final grade
     * @throws IllegalArgumentException if any of the parameters are null, if the coordinator
     * does not have the permission, if it does not exist an infraction committed
     * by that team or if the Hackathon is not in {@link HackathonStateType#IN_CORSO} o {@link HackathonStateType#IN_VALUTAZIONE}
     */
    @Transactional
    public void penalizeTeam(UUID coordinator, UUID team, float points) {

        UtenteRegistrato coord = userRepository.findById(coordinator).orElseThrow(
                () -> new IllegalArgumentException("coordinator cannot be null"));
        Team t = teamRepository.findById(team).orElseThrow(
                () -> new IllegalArgumentException("team to expel cannot be null"));
        if(points <= 0) throw new IllegalArgumentException("number of points invalid");
        Hackathon h = t.getHackathon();

        if (!coord.hasPermission(Permission.CAN_PENALIZE_TEAM, h)
                || !(h.getStateType().equals(HackathonStateType.IN_CORSO) ||
                !h.getStateType().equals(HackathonStateType.IN_VALUTAZIONE)))
            throw new UnsupportedOperationException("cannot perform this action");

        h.removeInfractionByTeam(t);
        hackathonRepository.save(h);
        Float grade = t.getGrade();
        grade = grade - points;
        t.setGrade(grade);
        teamRepository.save(t);
        EventManager.getInstance().notify(PENALIZZAZIONE_TEAM, t.getTeamMembersList(), h);
    }

    /**
     * Reports an {@link Infraction} committed by a {@link Team}
     * @param mentor the mentor that wants to report the infraction
     * @param dto the dto with the information regarding the infraction
     * @throws IllegalArgumentException if the parameters are null, if the dto is not valid
     * if the mentor does not have the permission to perform such operation or if the {@link Hackathon} is not
     * in {@link HackathonStateType#IN_CORSO} or {@link HackathonStateType#IN_VALUTAZIONE}
     */
    @Transactional
    public void reportInfraction(UUID mentor, InfractionDTO dto) {

        UtenteRegistrato m = userRepository.findById(mentor).orElseThrow(
                () -> new IllegalArgumentException("mentor cannt be null"));
        if(dto == null) throw new IllegalArgumentException("dto cannot be null");
        if(!checkInfractionData(dto)) throw new IllegalArgumentException("dto is not valid");
        Team t = teamRepository.findById(dto.team()).orElseThrow(
                () -> new IllegalArgumentException("team cannot be null"));
        Hackathon h = t.getHackathon();
        if(!m.hasPermission(Permission.CAN_REPORT_INFRACTION, h) ||
                h.getStateType().equals(HackathonStateType.IN_ISCRIZIONE) ||
                h.getStateType().equals(HackathonStateType.CONCLUSO))
            throw new IllegalArgumentException("cannot perform this operation");

        Infraction infraction = new Infraction(t, dto.description(), dto.type());
        h.addInfraction(infraction);
        hackathonRepository.save(h);
        EventManager.getInstance().notify(ILLECITO, List.of(h.getCoordinator()), h);
    }

    /**
     * Checks if the information in the DTO is completed and can be used to report a new {@link Infraction}
     * @param dto the dto containing the information
     * @return true if the dto is complete ad valid, false if not
     * @author Giorgia Branchesi
     */
    private boolean checkInfractionData(InfractionDTO dto) {

        if(dto.team() == null) return false;
        if(dto.description() == null || dto.description().isEmpty()) return false;
        return dto.type() != null;
    }
}