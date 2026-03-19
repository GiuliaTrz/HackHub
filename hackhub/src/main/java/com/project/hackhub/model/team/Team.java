package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;

import java.util.List;
import java.util.UUID;

public class Team {

    private String name;

    private UUID id;

    private List<Invito> invitationList;

    private List<UtenteRegistrato> teamMembersList;

    private UtenteRegistrato teamLeader;

    public List<UtenteRegistrato> getTeamMembersList(){
        return this.teamMembersList;
    }

    public void setTeamLeader(UtenteRegistrato u){
        this.teamLeader = u;
    }

    public void addTeamMember(UtenteRegistrato u){
            teamMembersList.add(u);
    }

    public boolean removeInvitationFromList(Invito i) {
        return invitationList.remove(i);
    }

    public void addInvitation(Invito i){
        invitationList.add(u);
    }

}
