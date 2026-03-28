package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@Entity @NoArgsConstructor
public class Team {

    private String name;

    @Id @GeneratedValue
    private UUID id;

    @OneToOne
    private Hackathon hackathon;

    @OneToMany
    private List<Invito> invitationList;

    @OneToMany
    private List<UtenteRegistrato> teamMembersList;

    @OneToOne
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
