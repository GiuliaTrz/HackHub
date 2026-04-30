package com.project.hackhub.handler;

import com.project.hackhub.dto.InfractionDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Infraction;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.observer.EventManager;
import com.project.hackhub.observer.EventType;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.project.hackhub.observer.EventType.INFRACTION;
import static com.project.hackhub.observer.EventType.PENALIZED_TEAM;

@Component
@RequiredArgsConstructor
public class InfractionHandler {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    /**
     * Deletes an infraction report.
     * Since Infraction is an @Embeddable without ID, it is identified via hackathonId and index.
     *
     * @param deleterId       ID of the user (organizer or mentor)
     * @param hackathonId     ID of the hackathon
     * @param infractionIndex position of the infraction in the list
     * @author Giulia Trozzi
     */
    @Transactional
    public void deleteInfraction(UUID deleterId, UUID hackathonId, int infractionIndex) {
         User deleter = userRepository.findById(deleterId)
                 .orElseThrow(() -> new IllegalArgumentException("User not found"));
         Hackathon hackathon = hackathonRepository.findById(hackathonId)
                 .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

         boolean isOrganizer = deleter.equals(hackathon.getCoordinator());
         boolean isMentor = hackathon.getMentorsList().contains(deleter);
         if (!isOrganizer && !isMentor) {
             throw new UnsupportedOperationException("Only organizer or mentor can delete an infraction");
         }

         if (infractionIndex < 0 || infractionIndex >= hackathon.getInfractions().size()) {
             throw new IllegalArgumentException("Invalid infraction index");
         }

         hackathon.getInfractions().remove(infractionIndex);
         hackathonRepository.save(hackathon);
    }

    /**
     * Expels a team from a Hackathon
     * @param team the team to expel
     * @param coordinator the coordinator of the Hackathon
     * @throws IllegalArgumentException if the parameters do not exist in their repository
     * or if the state of the Hackathon is not {@link HackathonStateType#ONGOING} or {@link HackathonStateType#APPRAISAL}
     * or if the coordinator does not have the permission to expel the team
     * @author Giorgia Branchesi
     */
    @Transactional
    public void expelTeam(UUID team, UUID coordinator) {

        User coord = userRepository.findById(coordinator).orElseThrow(() -> new IllegalArgumentException("coordinator cannot be null"));
        Team t = teamRepository.findById(team).orElseThrow(() -> new IllegalArgumentException("team to expel cannot be null"));
        Hackathon h = t.getHackathon();

        if(!coord.hasPermission(Permission.CAN_EXPEL_TEAM, h)
           || !(h.getStateType().equals(HackathonStateType.ONGOING) ||
                !h.getStateType().equals(HackathonStateType.APPRAISAL)))
            throw new UnsupportedOperationException("cannot perform this action");

        h.removeTeam(t);
        h.removeInfractionByTeam(t);
        hackathonRepository.save(h);
        List<User> teamMembers = t.getTeamMembersList();
        EventManager.getInstance().notify(EventType.EXPULSION_TEAM, teamMembers, h);
        teamRepository.delete(t);
    }

    /**
     * Lets a coordinator handle an infraction
     * @param coordinator the coordinator
     * @param team the team that committed the infraction
     * @throws IllegalArgumentException if any of the parameters are null, if the coordinator
     * does not have the permission, if it does not exist an infraction committed
     * by that team or if the Hackathon is not in {@link HackathonStateType#ONGOING} or {@link HackathonStateType#APPRAISAL}
     */
    @Transactional
    public void handleInfraction(UUID coordinator, UUID team) {

        User coord = userRepository.findById(coordinator).orElseThrow(
                () -> new IllegalArgumentException("coordinator cannot be null"));
        Team t = teamRepository.findById(team).orElseThrow(
                () -> new IllegalArgumentException("team to expel cannot be null"));
        Hackathon h = t.getHackathon();
        Infraction i = hackathonRepository.findInfractionByTeam(h, t).orElseThrow(
                () -> new IllegalArgumentException("infraction does not exist"));

        if (!coord.hasPermission(Permission.CAN_MANAGE_INFRACTIONS, h)
                || !(h.getStateType().equals(HackathonStateType.ONGOING) ||
                !h.getStateType().equals(HackathonStateType.APPRAISAL)))
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
     * by that team or if the Hackathon is not in {@link HackathonStateType#ONGOING} or {@link HackathonStateType#APPRAISAL}
     */
    @Transactional
    public void penalizeTeam(UUID coordinator, UUID team, float points) {

        User coord = userRepository.findById(coordinator).orElseThrow(
                () -> new IllegalArgumentException("coordinator cannot be null"));
        Team t = teamRepository.findById(team).orElseThrow(
                () -> new IllegalArgumentException("team to expel cannot be null"));
        if(points <= 0) throw new IllegalArgumentException("number of points invalid");
        Hackathon h = t.getHackathon();

        if (!coord.hasPermission(Permission.CAN_PENALIZE_TEAM, h)
                || !(h.getStateType().equals(HackathonStateType.ONGOING) ||
                !h.getStateType().equals(HackathonStateType.APPRAISAL)))
            throw new UnsupportedOperationException("cannot perform this action");

        h.removeInfractionByTeam(t);
        hackathonRepository.save(h);
        Float grade = t.getGrade();
        grade = grade - points;
        t.setGrade(grade);
        teamRepository.save(t);
        EventManager.getInstance().notify(PENALIZED_TEAM, t.getTeamMembersList(), h);
    }

    /**
     * Reports an {@link Infraction} committed by a {@link Team}
     * @param mentor the mentor that wants to report the infraction
     * @param dto the dto with the information regarding the infraction
     * @throws IllegalArgumentException if the parameters are null, if the dto is not valid
     * if the mentor does not have the permission to perform such operation or if the {@link Hackathon} is not
     * in {@link HackathonStateType#ONGOING} or {@link HackathonStateType#APPRAISAL}
     */
    @Transactional
    public void reportInfraction(UUID mentor, InfractionDTO dto) {

         User m = userRepository.findById(mentor).orElseThrow(
                 () -> new IllegalArgumentException("Mentor cannot be null"));
        if(dto == null) throw new IllegalArgumentException("dto cannot be null");
        if(!checkInfractionData(dto)) throw new IllegalArgumentException("dto is not valid");
        Team t = teamRepository.findById(dto.team()).orElseThrow(
                () -> new IllegalArgumentException("team cannot be null"));
        Hackathon h = t.getHackathon();
        if(!m.hasPermission(Permission.CAN_REPORT_INFRACTION, h) ||
                h.getStateType().equals(HackathonStateType.SUBSCRIPTION_PHASE) ||
                h.getStateType().equals(HackathonStateType.CONCLUDED))
            throw new IllegalArgumentException("cannot perform this operation");

        Infraction infraction = new Infraction(t, dto.description(), dto.type());
        h.addInfraction(infraction);
        hackathonRepository.save(h);
        EventManager.getInstance().notify(INFRACTION, List.of(h.getCoordinator()), h);
    }

    /**
     * Checks if the information in the DTO is completed and can be used to report a new {@link Infraction}
     * @param dto the dto containing the information
     * @return true if the dto is complete and valid, false if not
     * @author Giorgia Branchesi
     */
    private boolean checkInfractionData(InfractionDTO dto) {

        if(dto.team() == null) return false;
        if(dto.description() == null || dto.description().isEmpty()) return false;
        return dto.type() != null;
    }
}