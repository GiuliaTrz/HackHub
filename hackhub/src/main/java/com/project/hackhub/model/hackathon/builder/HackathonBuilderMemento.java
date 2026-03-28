package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Hackathon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity @NoArgsConstructor
public class HackathonBuilderMemento implements Memento {

    @Id @GeneratedValue
    private UUID id;

    @ManyToOne
    private Hackathon hackathon;

    public HackathonBuilderMemento(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    @Override
    public Hackathon getState() {
        return hackathon;
    }
}
