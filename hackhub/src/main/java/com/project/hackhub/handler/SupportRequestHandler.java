package com.project.hackhub.handler;

import com.project.hackhub.dto.AidRequestDTO;
import com.project.hackhub.model.calendar.CalendarAdapter;
import com.project.hackhub.model.calendar.Slot;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.AidRequestType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public class SupportRequestHandler {

    private final CalendarAdapter calendarAdapter;
    private final HackathonRepository hackathonRepository;

    private final UtenteRegistratoRepository utenteRepository;

    public SupportRequestHandler(CalendarAdapter calendarAdapter, HackathonRepository hackathonRepository, UtenteRegistratoRepository utenteRepository) {
        this.calendarAdapter = calendarAdapter;
        this.hackathonRepository = hackathonRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Retrieves the list of available time slots for a call proposal.
     * @param u The registered user requesting the slots.
     * @param c The calendarAdapter that handles timeslots for a given hackathon
     * @return A {@code List} of available {@code Slot} objects.
     * {@code null} if the hackathon state type is not "IN_CORSO"
     * @throws UnsupportedOperationException if the user lacks the {@code CAN_PROPOSE_CALL} permission.
     * @author Chiara Marinucci
     */
    public List<Slot> getAvailableSlots(UtenteRegistrato u, CalendarAdapter c){
        if(c.getHackathon().getState().getStateType() == HackathonStateType.IN_CORSO) {
            if (!u.hasPermission(Permission.CAN_PROPOSE_CALL, c.getHackathon()))
                throw new UnsupportedOperationException("Azione non permessa.");
            return calendarAdapter.getAvailableSlots();
        }
        return null;
    }

    /**
     *Proposes a call to a team that may benefit from aid but has not yet send an aidRequest.
     * Adds a new aid request in the hackathon's aidrequest list booked for a specific time slot and saves the changes.
     * Updates the team's pending request flag.
     * @param u the registered user that starts the action.
     * @param slot the timeslot chosen for the proposed call.
     * @param t the team the call is proposed to.
     * @return {@code true} if the slot was successfully reserved and the proposal created
     * {@code false} if the slot is no longer available or if the hackathon instance is not in the state IN_CORSO
     * @author Chiara Marinucci
     */
    public boolean proposeCall(UtenteRegistrato u, Slot slot, Team t){
        if(t.getHackathon().getState().getStateType() == HackathonStateType.IN_CORSO){
            if(!u.hasPermission(Permission.CAN_PROPOSE_CALL, t.getHackathon()))
                throw new UnsupportedOperationException("Azione non permessa.");
            boolean removed = calendarAdapter.removeSlot(slot);
            if(removed) {
                AidRequest a = new AidRequest(t, AidRequestType.CALL_PROPOSAL);
                t.getHackathon().addAidRequest(a);
                t.setPendingCallProposal(true);
                hackathonRepository.save(t.getHackathon());
                return true;}
        }
        return false;
    }

    /**
     * Processes an aid request for a specific team that is parteking in a certain hackathon.
     * @param u the registered user performing the request.
     * @param dto data transfer object containing team and request details.
     * @return {@code true} if the request is successfully created and saved.
     * {@code false} if hackathon is not in state "IN_CORSO", data is invalid or the team already has a pending request.
     * @throws IllegalArgumentException if dto or team is null
     * @throws UnsupportedOperationException if the user lacks required permission.
     * @author Chiara Marinucci
     */
    public boolean sendAidRequest(UtenteRegistrato u,@NonNull AidRequestDTO dto){
        if(dto.team().getHackathon().getState().getStateType() == HackathonStateType.IN_CORSO) {
            if (!u.hasPermission(Permission.CAN_SEND_AID_REQUEST, dto.team().getHackathon()))
                throw new UnsupportedOperationException("Azione non permessa");
            if (checkAidRequestData(dto)) {
                AidRequest aidRequest = new AidRequest(dto.team(), dto.type(), dto.description());
                dto.team().getHackathon().addAidRequest(aidRequest);
                dto.team().setPendingCallProposal(true);
                hackathonRepository.save(dto.team().getHackathon());
                return true;
            }
        }
        return false;
    }

    /**
     * Validates the aid request data and ensures uniqueness.
     * @param dto data to check
     * @return {@code true} if fields are complete and no prior request exists for the team.
     * {@code false} otherwise
     * @author Chiara Marinucci
     */
    private boolean checkAidRequestData(AidRequestDTO dto){
        if(dto.team() == null || dto.type() == null || dto.description() == null)
            return false;
        List<AidRequest> aidRequests = dto.team().getHackathon().getAidRequests();
        return aidRequests.stream().noneMatch(a -> a.getTeam().equals(dto.team()));
    }

    /**
     * Elimina una richiesta di supporto. Si assume che al massimo ci sia una richiesta per team.
     * @param requesterId ID utente (team leader o mentore)
     * @param hackathonId ID hackathon
     * @param teamId      ID team
     * @author Giulia Trozzi
     */
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
        team.setPendingCallProposal(false);
        hackathonRepository.save(hackathon);
    }

    /**
     * Visualizza tutte le richieste di supporto di un hackathon.
     * @param viewerId    ID utente (mentore o organizzatore)
     * @param hackathonId ID hackathon
     * @return lista di AidRequest
     * @author Giulia Trozzi
     */
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
