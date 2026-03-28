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
    private Localita localita;

    @Embedded
    private IntervalloTemporale intervalloTemporale;

    @Id @GeneratedValue
    private UUID id;

    public Prenotazione(Localita localita, IntervalloTemporale intervalloTemporale){
        this.localita = localita;
        this.intervalloTemporale = intervalloTemporale;
    }
}
