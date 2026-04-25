package com.project.hackhub.model.team;

import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Invito {

    @ManyToOne
    private Team mittente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private UtenteRegistrato destinatario;

    private boolean pendente;

    @Id
    @GeneratedValue
    private UUID id;

    public Invito(Team team, UtenteRegistrato user) {
        if (team == null || user == null)
            throw new IllegalArgumentException("Invalid parameters.");

        this.mittente = team;
        this.destinatario = user;
        this.pendente = true;
    }

    public void accept() {
        if (!pendente)
            throw new IllegalStateException("Invite already processed.");

        this.pendente = false;
    }

    public void reject() {
        if (!pendente)
            throw new IllegalStateException("Invite already processed.");

        this.pendente = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Invito invito = (Invito) o;
        return Objects.equals(id, invito.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}