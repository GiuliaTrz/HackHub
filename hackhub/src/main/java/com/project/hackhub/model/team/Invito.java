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

    @OneToOne
    private Team team;

    @Id @GeneratedValue
    private UUID id;

}
