package com.project.hackhub.model.team;

import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.UUID;

@Entity
public class Invito {

    @OneToOne
    private UtenteRegistrato mittente;

    @OneToOne
    private UtenteRegistrato destinatario;

    private boolean pendente;

<<<<<<< Updated upstream
    @OneToOne
    private Team team;

    @Id @GeneratedValue
    private UUID id;

}
=======
    @Id
    @GeneratedValue
    private UUID id;

    public Invito(Team team, UtenteRegistrato user) {
        if (team == null || user == null)
            throw new IllegalArgumentException("Parametri non validi.");

        this.mittente = team;
        this.destinatario = user;
        this.pendente = true;
    }

    public void accetta() {
        if (!pendente)
            throw new IllegalStateException("Invito già gestito.");

        this.pendente = false;
    }

    public void rifiuta() {
        if (!pendente)
            throw new IllegalStateException("Invito già gestito.");

        this.pendente = false;
    }
}
>>>>>>> Stashed changes
