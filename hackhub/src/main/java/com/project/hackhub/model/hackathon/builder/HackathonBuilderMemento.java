package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity @NoArgsConstructor
public class HackathonBuilderMemento implements Memento {

    @Id @GeneratedValue
    private UUID id;

    @Setter
    @OneToOne
    @JoinColumn(unique = true)
    private UtenteRegistrato author;

    @ManyToOne(cascade = CascadeType.ALL)
    private Hackathon hackathon;

    public HackathonBuilderMemento(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    @Override
    public Hackathon getState() {
        return hackathon;
    }
}
