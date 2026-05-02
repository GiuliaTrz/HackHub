package com.project.hackhub.service.calendar;

import com.project.hackhub.model.hackathon.Hackathon;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * A simulated adapter service that would be used to interact with an external Calendar system.
 */
@Component
public class CalendarAdapter {

    public List<Slot> getAvailableSlots(Hackathon hackathon) {
        return simulateExternalCalendar(hackathon);
    }

    /**
     * This method simulates an interaction with an external calendar system
     * by returning a list of mock slots for the given hackathon. As such, it is to
     * be replaced with actual logic to interact with the external calendar system in a real implementation.
     * @param hackathon the hackathon for which to retrieve available slots for call proposal
     * @return a list of available slots for call proposal for the given hackathon
     */
    private List<Slot> simulateExternalCalendar(Hackathon hackathon) {
        List<Slot> mockSlots = new ArrayList<>();
        mockSlots.add(
                new Slot(LocalDateTime.of(2026, Month.MAY,15,10,30,0),
                        LocalDateTime.of(2026, Month.MAY,15,10,45,0)));
        mockSlots.add(
                new Slot(LocalDateTime.of(2026, Month.MAY,15,10,00,0),
                        LocalDateTime.of(2026, Month.MAY,15,10,15,0)));
        mockSlots.add(
                new Slot(LocalDateTime.of(2026, Month.MAY,15,11,15,0),
                        LocalDateTime.of(2026, Month.MAY,15,11,30,0)));
        return mockSlots;
    }
    //simulated
    public boolean removeSlot(Hackathon hackathon, Slot slot){
        return true;
    }
}
