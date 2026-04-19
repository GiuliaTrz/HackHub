package com.project.hackhub.handler;

import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;

public class SubmissionHandler {
    private TeamRepository teamRepository;
    private UtenteRegistratoRepository utenteRegistratoRepository;

    public SubmissionHandler(TeamRepository teamRepository, UtenteRegistratoRepository utenteRegistratoRepository) {
        this.teamRepository = teamRepository;
        this.utenteRegistratoRepository = utenteRegistratoRepository;
    }


}
