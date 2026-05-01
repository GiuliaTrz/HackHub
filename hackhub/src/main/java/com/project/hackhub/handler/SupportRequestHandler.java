package com.project.hackhub.handler;

import com.project.hackhub.dto.AidRequestDTO;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.service.calendar.CalendarAdapter;
import com.project.hackhub.service.calendar.Slot;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.AidRequestType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.model.user.User;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SupportRequestHandler {

    private final CalendarAdapter calendarAdapter;
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public SupportRequestHandler(CalendarAdapter calendarAdapter, HackathonRepository hackathonRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.calendarAdapter = calendarAdapter;
        this.hackathonRepository = hackathonRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Retrieves the list of available time slots for a call proposal.
     * @param user The id of a user requesting the slots.
     * @param hackathon The id for a given hackathon
     * @return A {@code List} of available {@code Slot} objects.
     * @throws IllegalStateException if the Hackathon state is not "IN_CORSO"
     * @throws IllegalArgumentException if the user is not a {@code UtenteRegistrato}
     * @throws UnsupportedOperationException if the user lacks the required permission.
     * @author Chiara Marinucci
     */
    @Transactional
    public List<Slot> getAvailableSlots(UUID user, UUID hackathon){
        Hackathon h = this.hackathonRepository.findById(hackathon)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        if(h.getState().getStateType() != HackathonStateType.ONGOING)
            throw new IllegalStateException("Hackathon is not IN_CORSO");
        User u = this.userRepository.findById(user)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
            if (!u.hasPermission(Permission.CAN_PROPOSE_CALL, h))
                throw new UnsupportedOperationException("User does not have required permission");
            return this.calendarAdapter.getAvailableSlots(h);
    }

    /**
     * Proposes a call to a team that a mentor reckons may benefit from aid.
     * Adds a new aid request in the hackathon's aidrequest list booked for a specific time slot and saves the changes.
     * Updates the team's pending request flag.
     *
     * @param mentor the id of a user that starts the action.
     * @param slot   the timeslot chosen for the proposed call.
     * @param team   the id of a team the call is proposed to.
     * @throws IllegalArgumentException      if the user is not a UtenteRegistrato
     * @throws IllegalStateException         if the Hackathon state is not "IN_CORSO"
     * @throws UnsupportedOperationException if the user lacks the required permission.
     * @author Chiara Marinucci
     */
    @Transactional
    public void proposeCall(UUID mentor, Slot slot, UUID team){
        Team t = this.hackathonRepository.findByTeamId(team)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        if(t.getHackathon().getState().getStateType() != HackathonStateType.ONGOING)
            throw new IllegalStateException("Hackathon is not IN_CORSO");
        User u = this.userRepository.findById(mentor)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(!u.hasPermission(Permission.CAN_PROPOSE_CALL, t.getHackathon()))
                throw new UnsupportedOperationException("User does not have required permission");
        if(t.isHasPendingCallProposal())
            throw new IllegalStateException("Team already has a pending call proposal");
        boolean removed = this.calendarAdapter.removeSlot(t.getHackathon(), slot);
            if(removed) {
                AidRequest a = new AidRequest(t, AidRequestType.CALL_PROPOSAL, slot);
                t.getHackathon().addAidRequest(a);
                t.setHasPendingCallProposal(true);
                hackathonRepository.save(t.getHackathon());
            }
    }

    /**
     * Processes an aid request for a specific team that is parteking in a certain hackathon.
     *
     * @param leader the id of a registered user performing the request.
     * @param dto    data transfer object containing team and request details.
     * @throws IllegalArgumentException      if dto or team is null
     * @throws IllegalStateException         if hackathon is not in state "IN_CORSO"
     * @throws UnsupportedOperationException if the user lacks required permission.
     * @author Chiara Marinucci
     */
    @Transactional
    public void sendAidRequest(UUID leader, AidRequestDTO dto){
        if(dto == null) return;
        Team realTeam = this.teamRepository.findById(dto.team())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        User u =  this.userRepository.findById(leader)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(realTeam.getHackathon().getState().getStateType() != HackathonStateType.ONGOING)
                throw new IllegalStateException("Hackathon is not ONGOING");
        if (!u.hasPermission(Permission.CAN_SEND_AID_REQUEST, realTeam.getHackathon()))
                throw new UnsupportedOperationException("User does not have required permission");
        if (checkAidRequestData(dto, realTeam)) {
                AidRequest aidRequest = new AidRequest(realTeam, dto.type(), dto.description(), null);
                realTeam.getHackathon().addAidRequest(aidRequest);
                realTeam.setHasPendingCallProposal(true);
                hackathonRepository.save(realTeam.getHackathon());
        } else
            throw new IllegalStateException("Team already has a pending request or fields are incomplete");
    }

    /**
     * Validates the aid request data and ensures uniqueness.
     * @param dto data to check
     * @param t team for which the request is being made
     * @return {@code true} if fields are complete and no prior request exists for the team.
     * {@code false} otherwise
     * @author Chiara Marinucci
     */
    private boolean checkAidRequestData(AidRequestDTO dto, Team t){
        if(dto.team() == null || dto.description().isBlank()
        || dto.type() == null)
            return false;
        if(t==null)
            return false;
        return !t.isHasPendingCallProposal();
    }

    /**
     * Deletes an AidRequest for a specific team in a given hackathon.
     * Only the team leader or a mentor can perform this action.
     * @param requesterId user ID
     * @param hackathonId hackathon ID
     * @param teamId      team ID
     * @author Giulia Trozzi
     */
    @Transactional
    public void deleteSupportRequest(UUID requesterId, UUID hackathonId, UUID teamId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        Team team = hackathon.getTeamsList().stream()
                .filter(t -> t.getId().equals(teamId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team does not partecipate in this hackathon"));

        if (!requester.hasPermission(Permission.CAN_HANDLE_AID_REQUEST, hackathon))
            throw new UnsupportedOperationException("User does not have required permission");

        AidRequest toRemove = hackathon.getAidRequests().stream()
                .filter(r -> r.getTeam().equals(team))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No support request active for this team"));

        hackathon.getAidRequests().remove(toRemove);
        team.setHasPendingCallProposal(false);
        hackathonRepository.save(hackathon);
    }

    /**
     * Gets all AidRequests of an hackathon
     * @param viewerId    user ID (mentor or coordinator
     * @param hackathonId hackathon ID
     * @return AidRequest list
     * @author Giulia Trozzi
     */
    @Transactional
    public List<AidRequest> getAllSupportRequests(UUID viewerId, UUID hackathonId) {
        User viewer = userRepository.findById(viewerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        if(!viewer.hasPermission(Permission.STAFF_PERMISSION, hackathon))
            throw new UnsupportedOperationException("user lacks required permissions for the operation");

        return hackathon.getAidRequests();
    }
}
