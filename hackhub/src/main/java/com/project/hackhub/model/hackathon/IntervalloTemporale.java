package com.project.hackhub.model.hackathon;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record IntervalloTemporale(LocalDate startDate, LocalDate endDate) {

    public IntervalloTemporale {
        if (startDate == null || endDate == null)
            throw new IllegalArgumentException("Dates cannot be null.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("End date cannot be before start date.");
    }

    public boolean overlapsWith(IntervalloTemporale other) {
        if (other == null)
            throw new IllegalArgumentException("Interval cannot be null.");

        // overlap se:
        // start <= other.end AND other.start <= end
        return !this.endDate.isBefore(other.startDate)
                && !other.endDate.isBefore(this.startDate);
    }
}