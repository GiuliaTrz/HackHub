package com.project.hackhub.service.calendar;

import com.project.hackhub.model.hackathon.Hackathon;

import java.util.ArrayList;
import java.util.List;

/**
 * A simulated adapter service that would be used to interact with an external Calendar system.
 */

public class CalendarAdapter {

    public List<Slot> getAvailableSlots(Hackathon hackathon) {
        return simlutaleExternalCalendar(hackathon);
    }

    private List<Slot> simlutaleExternalCalendar(Hackathon hackathon) {
        List<Slot> mockSlots = new ArrayList<>();
        return mockSlots;
    }
    public boolean removeSlot(Hackathon hackathon, Slot slot){
        return true;
    }
}
