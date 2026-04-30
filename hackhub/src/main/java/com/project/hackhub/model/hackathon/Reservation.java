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
public class Reservation {

    @Embedded
    private Location location;

    @Embedded
    private TimeInterval timeInterval;

    @Id
    @GeneratedValue
    private UUID id;

    public Reservation(Location location, TimeInterval timeInterval){
        if (location == null)
            throw new IllegalArgumentException("Location cannot be null.");

        if (timeInterval == null)
            throw new IllegalArgumentException("Time interval cannot be null.");

        this.location = location;
        this.timeInterval = timeInterval;
    }

    /**
     * Verifies if this reservation overlaps with another reservation.
     *
     * @param other the reservation to check overlap with; cannot be null
     * @return true if the time intervals overlap, false otherwise
     * @throws IllegalArgumentException if the reservation to compare is null
     * @throws IllegalStateException if one of the time intervals is not set
     *
     * @author Giulia Trozzi
     */
    public boolean overlapsWith(Reservation other) {
        if (other == null)
            throw new IllegalArgumentException("Reservation cannot be null.");

        if (this.timeInterval == null || other.timeInterval == null)
            throw new IllegalStateException("Time interval not set.");

        return this.timeInterval.overlapsWith(other.timeInterval);
    }

    /**
     * Checks if this reservation has the same {@link Location} as another reservation.
     *
     * @param other the reservation to compare; cannot be null
     * @return true if the two reservations share the same location, false otherwise
     * @throws IllegalArgumentException if the reservation to compare is null
     * @throws IllegalStateException if the location of one of the reservations is not set
     *
     * @author Giulia Trozzi
     */
    public boolean isSameLocation(Reservation other) {
        if (other == null)
            throw new IllegalArgumentException("Reservation cannot be null.");

        if (this.location == null || other.location == null)
            throw new IllegalStateException("Location not set.");

        return this.location.equals(other.location);
    }

}