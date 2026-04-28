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
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SupportRequestHandler {

    private final CalendarAdapter calendarAdapter;
    private final HackathonRepository hackathonRepository;
    private final UtenteRegistratoRepository utenteRepository;
    private final TeamRepository teamRepository;

    public SupportRequestHandler(CalendarAdapter calendarAdapter, HackathonRepository hackathonRepository, UtenteRegistratoRepository utenteRepository, TeamRepository teamRepository) {
        this.calendarAdapter = calendarAdapter;
        this.hackathonRepository = hackathonRepository;
        this.utenteRepository = utenteRepository;
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
        if(h.getState().getStateType() != HackathonStateType.IN_CORSO)
            throw new IllegalStateException("Hackathon is not IN_CORSO");
        UtenteRegistrato u = this.utenteRepository.findById(user)
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
        if(t.getHackathon().getState().getStateType() != HackathonStateType.IN_CORSO)
            throw new IllegalStateException("Hackathon is not IN_CORSO");
        UtenteRegistrato u = this.utenteRepository.findById(mentor)
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
        UtenteRegistrato u =  this.utenteRepository.findById(leader)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(realTeam.getHackathon().getState().getStateType() != HackathonStateType.IN_CORSO)
                throw new IllegalStateException("Hackathon is not IN_CORSO");
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
     * Elimina una richiesta di supporto. Si assume che al massimo ci sia una richiesta per team.
     * @param requesterId ID utente (team leader o mentore)
     * @param hackathonId ID hackathon
     * @param teamId      ID team
     * @author Giulia Trozzi
     */
    @Transactional
    public void deleteSupportRequest(UUID requesterId, UUID hackathonId, UUID teamId) {
        UtenteRegistrato requester = utenteRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        Team team = hackathon.getTeamsList().stream()
                .filter(t -> t.getId().equals(teamId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato nell'hackathon"));

        boolean isTeamLeader = team.getTeamLeader().getId().equals(requesterId);
        boolean isMentor = hackathon.getMentorsList().contains(requester);
        if (!isTeamLeader && !isMentor) {
            throw new UnsupportedOperationException("Solo team leader o mentore possono eliminare la richiesta");
        }

        AidRequest toRemove = hackathon.getAidRequests().stream()
                .filter(r -> r.getTeam().equals(team))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nessuna richiesta attiva per questo team"));

        hackathon.getAidRequests().remove(toRemove);
        team.setHasPendingCallProposal(false);
        hackathonRepository.save(hackathon);
    }

    /**
     * Visualizza tutte le richieste di supporto di un hackathon.
     * @param viewerId    ID utente (mentore o organizzatore)
     * @param hackathonId ID hackathon
     * @return lista di AidRequest
     * @author Giulia Trozzi
     */
    @Transactional
    public List<AidRequest> getAllSupportRequests(UUID viewerId, UUID hackathonId) {
        UtenteRegistrato viewer = utenteRepository.findById(viewerId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        boolean isMentor = hackathon.getMentorsList().contains(viewer);
        boolean isOrganizer = viewer.equals(hackathon.getCoordinator());

        if (!isMentor && !isOrganizer) {
            throw new UnsupportedOperationException("Permessi insufficienti per visualizzare le richieste");
        }
        return hackathon.getAidRequests();
    }
}
