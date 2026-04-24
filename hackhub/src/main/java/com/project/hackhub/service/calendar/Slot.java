package com.project.hackhub.service.calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter @Setter
@NoArgsConstructor
public class Slot {
    @Column(name = "\"start\"")
    private LocalDateTime start;
    @Column(name = "\"end\"")
    private LocalDateTime end;

    public Slot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }
}
