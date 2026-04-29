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
     * Verifica se questa prenotazione si sovrappone con un'altra prenotazione.
     *
     * @param other la prenotazione con cui verificare la sovrapposizione; non può essere null
     * @return true se gli intervalli temporali si sovrappongono, false altrimenti
     * @throws IllegalArgumentException se la prenotazione da confrontare è null
     * @throws IllegalStateException se uno dei due intervalli temporali non è impostato
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
     * Controlla se questa prenotazione ha la stessa {@link Location} di un'altra prenotazione.
     *
     * @param other la prenotazione da confrontare; non può essere null
     * @return true se le due prenotazioni condividono la stessa location, false altrimenti
     * @throws IllegalArgumentException se la prenotazione da confrontare è null
     * @throws IllegalStateException se la location di una delle due prenotazioni non è impostata
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