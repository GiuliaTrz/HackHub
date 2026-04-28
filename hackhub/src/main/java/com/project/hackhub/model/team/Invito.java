package com.project.hackhub.model.team;

import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Invito {

    @ManyToOne
    private Team sender;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private UtenteRegistrato addresee;

    private boolean pending;

    @Id
    @GeneratedValue
    private UUID id;

    public Invito(Team team, UtenteRegistrato user) {
        if (team == null || user == null)
            throw new IllegalArgumentException("Invalid parameters.");

        this.sender = team;
        this.addresee = user;
        this.pending = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invito other = (Invito) o;

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}