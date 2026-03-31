package com.project.hackhub.model.calendar;

import com.project.hackhub.model.hackathon.Hackathon;
import java.util.List;


public class CalendarAdapter {

    private List<Slot> slotsList;
    private final Hackathon hackathon;

    public CalendarAdapter(Hackathon hackathon){
        this.hackathon = hackathon;
    }

    public Hackathon getHackathon(){
        return this.hackathon;
    }
    //TODO
    public List<Slot> getAvailableSlots() {
        return null;
    }
    //TODO
    public boolean removeSlot(Slot slot) {
        return false;
    }
}
