package com.project.hackhub.model.team;

import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Invito {

    @OneToOne
    private Team mittente;

    @OneToOne
    private UtenteRegistrato destinatario;

    private boolean pendente;

    @Id @GeneratedValue
    private UUID id;

    public Invito(Team team, UtenteRegistrato user) {
        this.mittente = team;
        this.destinatario = user;
        this.pendente = true;
    }
}
