package com.project.hackhub.model.hackathon;

import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Prenotazione {

    @Embedded
    private Localita location;

    @Embedded
    private IntervalloTemporale timeInterval;

    @Id
    @GeneratedValue
    private UUID id;

    public Prenotazione(Localita location, IntervalloTemporale timeInterval){
        if (location == null)
            throw new IllegalArgumentException("Location cannot be null.");

        if (timeInterval == null)
            throw new IllegalArgumentException("Time interval cannot be null.");

        this.location = location;
        this.timeInterval = timeInterval;
    }

    // --- BUSINESS LOGIC ---

    public void changeLocation(Localita newLocation) {
        if (newLocation == null)
            throw new IllegalArgumentException("New location cannot be null.");

        this.location = newLocation;
    }

    public void changeTimeInterval(IntervalloTemporale newInterval) {
        if (newInterval == null)
            throw new IllegalArgumentException("Time interval cannot be null.");

        this.timeInterval = newInterval;
    }

    public boolean overlapsWith(Prenotazione other) {
        if (other == null)
            throw new IllegalArgumentException("Reservation cannot be null.");

        if (this.timeInterval == null || other.timeInterval == null)
            throw new IllegalStateException("Time interval not set.");

        return this.timeInterval.overlapsWith(other.timeInterval);
    }

    public boolean isSameLocation(Prenotazione other) {
        if (other == null)
            throw new IllegalArgumentException("Reservation cannot be null.");

        if (this.location == null || other.location == null)
            throw new IllegalStateException("Location not set.");

        return this.location.equals(other.location);
    }
}