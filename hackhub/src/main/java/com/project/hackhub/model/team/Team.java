package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Team {
    private String name;

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private Hackathon hackathon;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invito> invitationList = new ArrayList<>();

    @OneToMany
    private List<UtenteRegistrato> teamMembersList = new ArrayList<>();

    @OneToOne
    private UtenteRegistrato teamLeader;

    private boolean pendingCallProposal;

    // --- BUSINESS LOGIC ---

    public void addTeamMember(UtenteRegistrato u) {
        if (u == null)
            throw new IllegalArgumentException("L'utente non può essere null.");

        if (teamMembersList.contains(u))
            throw new IllegalStateException("Utente già presente nel team.");

        if (hackathon != null &&
                teamMembersList.size() >= hackathon.getMaxTeamDimension())
            throw new IllegalStateException("Dimensione massima del team raggiunta.");

        teamMembersList.add(u);
    }

    public void removeTeamMember(UtenteRegistrato u) {
        if (u == null)
            throw new IllegalArgumentException("Utente non valido.");

        if (!teamMembersList.contains(u))
            throw new IllegalStateException("Utente non presente nel team.");

        if (u.equals(teamLeader))
            throw new IllegalStateException("Non puoi rimuovere il team leader.");

        teamMembersList.remove(u);
    }

    public boolean removeInvitationFromList(Invito i) {
        if (i == null)
            throw new IllegalArgumentException("Invito nullo.");

        return invitationList.remove(i);
    }

    public void addInvitation(Invito i) {
        if (i == null)
            throw new IllegalArgumentException("Invito nullo.");

        if (invitationList.contains(i))
            throw new IllegalStateException("Invito già presente.");

        invitationList.add(i);
    }

    public void setTeamLeader(UtenteRegistrato leader) {
        if (leader == null)
            throw new IllegalArgumentException("Leader non valido.");

        if (!teamMembersList.contains(leader))
            throw new IllegalStateException("Il leader deve essere membro del team.");

        this.teamLeader = leader;
    }
}