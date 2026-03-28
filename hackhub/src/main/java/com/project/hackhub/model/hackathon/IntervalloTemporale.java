package com.project.hackhub.model.hackathon;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record IntervalloTemporale (LocalDate startDate, LocalDate endDate ) {}
