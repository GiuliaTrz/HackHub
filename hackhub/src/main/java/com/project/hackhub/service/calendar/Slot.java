package com.project.hackhub.service.calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter @Setter
@NoArgsConstructor
public class Slot {
    @Column(name = "start")
    private LocalDateTime start;
    @Column(name = "end")
    private LocalDateTime end;

    public Slot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(start, slot.start) && Objects.equals(end, slot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
