package com.project.hackhub.service;

import com.project.hackhub.model.calendar.CalendarAdapter;
import com.project.hackhub.model.calendar.Slot;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.team.AidRequest;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.HackathonRepository;

import java.util.List;

public class SupportRequestHandler {

    private CalendarAdapter calendarAdapter;
    private HackathonRepository hackathonRepository;

    public List<Slot> getAvailableSlots(UtenteRegistrato u, Team t){
        if(!u.hasPermission(Permission.CAN_PROPOSE_CALL, t.getHackathon()))
            throw new UnsupportedOperationException("Azione non permessa.");
        return calendarAdapter.getAvailableSlots();
    }

    public boolean proposeCall(UtenteRegistrato u, Slot slot, Team t){
        if(!u.hasPermission(Permission.CAN_PROPOSE_CALL, t.getHackathon()))
            throw new UnsupportedOperationException("Azione non permessa.");
        boolean removed = calendarAdapter.removeSlot(slot);
        if(removed) {
            t.setPendingCallProposal(true);
            AidRequest a = new AidRequest(t, "callProposal");
            t.getHackathon().addAidRequest(a);
            hackathonRepository.save(t.getHackathon());
            return true;
        }
        return false;
    }

}
