package com.project.hackhub.model.hackathon;

import java.util.UUID;
import lombok.Getter;


@Getter
public class Prenotazione {

    private Localita localita;

    private IntervalloTemporale intervalloTemporale;

    private UUID id;

Prenotazione(Localita localita, IntervalloTemporale intervalloTemporale){
    this.localita = localita;
    this.intervalloTemporale = intervalloTemporale;
    }
}
