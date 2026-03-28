package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Team {

    private String name;

    private UUID id;

    private Hackathon hackathon;

    private List<Invito> invitationList;

    private List<UtenteRegistrato> teamMembersList;

    private UtenteRegistrato teamLeader;

    private boolean pendingCallProposal;

    //note: fix method names
    
    public void addTeamMember(UtenteRegistrato u){
            teamMembersList.add(u);
    }

    public boolean removeInvitationFromList(Invito i) {
        return invitationList.remove(i);
    }

    public void addInvitation(Invito i){
        invitationList.add(i);
    }

}
