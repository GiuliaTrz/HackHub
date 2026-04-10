package com.project.hackhub.handler;

import com.project.hackhub.dto.AidRequestDTO;
import com.project.hackhub.model.calendar.CalendarAdapter;
import com.project.hackhub.model.calendar.Slot;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.AidRequestType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.HackathonRepository;
import lombok.NonNull;

import java.util.List;

public class SupportRequestHandler {

    private final CalendarAdapter calendarAdapter;
    private final HackathonRepository hackathonRepository;


    public SupportRequestHandler(CalendarAdapter calendarAdapter, HackathonRepository hackathonRepository) {
        this.calendarAdapter = calendarAdapter;
        this.hackathonRepository = hackathonRepository;
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
}
