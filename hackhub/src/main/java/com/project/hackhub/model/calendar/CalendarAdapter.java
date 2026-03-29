package com.project.hackhub.model.calendar;

import com.project.hackhub.model.hackathon.state.HackathonStateType;
import lombok.Getter;
import java.util.List;

@Getter
public class CalendarAdapter {

    private List<Slot> slotsList;
    private HackathonStateType  hackathonStateType;

    //TODO
    public List<Slot> getAvailableSlots() {
        return null;
    }
    //TODO
    public boolean removeSlot(Slot slot) {
        return false;
    }
}
