package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.InvitoRepository;
import com.project.hackhub.repository.TeamRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.project.hackhub.service.UserStateService.changeUserState;

@Component
@RequiredArgsConstructor
public class UtenteRegistratoHandler {

    private final UtenteRegistratoRepository utenteRegistratoRepo;
    private final TeamRepository teamRepo;
    private final InvitoRepository invitoRepo;

    /**
     * Aggiunge un invito alla lista dell'utente.
     * @author Giulia Trozzi
     */
    public void addInvitation(UtenteRegistrato user, Invito invitation) {
        user.addInvitation(invitation);
        utenteRegistratoRepo.save(user);
    }

    /**
     * Accetta un invito a far parte di un team.
     *
     * @param i l'invito da accettare
     * @throws IllegalArgumentException      se l'invito è null
     * @throws UnsupportedOperationException se l'hackathon non è in stato IN_ISCRIZIONE
     *                                       o se l'utente non è disponibile
     * @author Giulia Trozzi
     */
    public void acceptInvitation(Invito i) {
        if (i == null)
            throw new IllegalArgumentException("The invitation cannot be null");

        UtenteRegistrato destinatario = i.getDestinatario();
        Team team = i.getMittente();
        Hackathon hackathon = team.getHackathon();

        if (!hackathon.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE)
                || !destinatario.isAvailable(hackathon.getReservation())) {
            throw new UnsupportedOperationException("Azione non permessa");
        }

        //rimuovi l'invito dalla lista dell'utente
        destinatario.removeInvitation(i);
        utenteRegistratoRepo.save(destinatario);

        //cambia lo stato dell'utente in MEMBRO_DEL_TEAM per questo hackathon
        changeUserState(destinatario, true, hackathon, UserStateType.MEMBRO_DEL_TEAM);

        //al'utente come membro del team
        team.addTeamMember(destinatario);
        teamRepo.save(team);

        //elimina l'invito dal database
        invitoRepo.delete(i);
    }

    /**
     * Rifiuta un invito a far parte di un team.
     *
     * @param i l'invito da rifiutare
     * @throws IllegalArgumentException      se l'invito è null
     * @throws UnsupportedOperationException se l'hackathon non è in stato IN_ISCRIZIONE
     * @author Giulia Trozzi
     */
    public void declineInvitation(Invito i) {
        if (i == null)
            throw new IllegalArgumentException("The invitation cannot be null");

        UtenteRegistrato destinatario = i.getDestinatario();
        Hackathon hackathon = i.getMittente().getHackathon();

        if (!hackathon.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE)) {
            throw new UnsupportedOperationException("Azione non permessa");
        }

        //rimuovi l'invito dalla lista dell'utente
        destinatario.removeInvitation(i);
        utenteRegistratoRepo.save(destinatario);

        //elimina l'invito dal database
        invitoRepo.delete(i);
    }
}