package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserState;
import com.project.hackhub.model.utente.state.UserStateFactory;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.AllArgsConstructor;

import static com.project.hackhub.service.UserStateService.changeUserState;

@AllArgsConstructor
public class UtenteRegistratoHandler {

    private final UtenteRegistratoRepository utenteRegistratoRepo;
    private final TeamRepository teamRepo;
    private final TeamHandler teamHandler;

    public void addInvitation(UtenteRegistrato user, Invito invitation) {

        user.addInvitation(invitation);
        utenteRegistratoRepo.save(user);

    }

}
