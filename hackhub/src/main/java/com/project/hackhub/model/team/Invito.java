package com.project.hackhub.model.team;

import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.UUID;

public class Invito {

    private UtenteRegistrato mittente;

    private UtenteRegistrato destinatario;

    private boolean pendente;

    private Team team;

    private UUID id;

}
