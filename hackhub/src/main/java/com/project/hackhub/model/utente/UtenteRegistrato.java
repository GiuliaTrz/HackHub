package com.project.hackhub.model.utente;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.utente.state.UserState;

import java.util.List;
import java.util.UUID;

public class UtenteRegistrato implements Utente{

    private UUID id;

    private List<Invito> invitationsList;

    private List<Prenotazione> reservationsList;

    private Anagrafica a;

    private UserState state;

}
