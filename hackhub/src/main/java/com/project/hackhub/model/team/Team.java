package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invito> invitationList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UtenteRegistrato> teamMembersList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "team_leader_id")
    private UtenteRegistrato teamLeader;

    private boolean pendingCallProposal;

    private Float grade;

    /**
     * Aggiunge un {@link UtenteRegistrato} alla lista dei membri del team.
     *
     * @param u l'utente da aggiungere; non può essere null
     * @throws IllegalArgumentException se l'utente è null
     * @throws IllegalStateException se l'utente è già presente nel team
     * o se la dimensione massima del team è stata raggiunta
     *
     * @author Giulia Trozzi
     */
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

    /**
     * Rimuove un {@link UtenteRegistrato} dalla lista dei membri del team.
     *
     * @param u l'utente da rimuovere; non può essere null
     * @throws IllegalArgumentException se l'utente è null
     * @throws IllegalStateException se l'utente non è presente nel team
     * o se si tenta di rimuovere il team leader
     *
     * @author Giulia Trozzi
     */
    public void removeTeamMember(UtenteRegistrato u) {
        if (u == null)
            throw new IllegalArgumentException("Utente non valido.");

        if (!teamMembersList.contains(u))
            throw new IllegalStateException("Utente non presente nel team.");

        if (u.equals(teamLeader))
            throw new IllegalStateException("Non puoi rimuovere il team leader.");

        teamMembersList.remove(u);
    }


    /**
     * Rimuove un {@link Invito} dalla lista degli inviti del team.
     *
     * @param i l'invito da rimuovere; non può essere null
     * @return true se l'invito è stato rimosso, false se non era presente nella lista
     * @throws IllegalArgumentException se l'invito è null
     *
     * @author Giulia Trozzi
     */
    public boolean removeInvitationFromList(Invito i) {
        if (i == null)
            throw new IllegalArgumentException("Invito nullo.");

        return invitationList.remove(i);
    }

    /**
     * Aggiunge un {@link Invito} alla lista degli inviti del team.
     *
     * @param i l'invito da aggiungere; non può essere null
     * @throws IllegalArgumentException se l'invito è null
     * @throws IllegalStateException se l'invito è già presente nella lista
     *
     * @author Giulia Trozzi
     */
    public void addInvitation(Invito i) {
        if (i == null)
            throw new IllegalArgumentException("Invito nullo.");

        if (invitationList.contains(i))
            throw new IllegalStateException("Invito già presente.");

        invitationList.add(i);
    }

    /**
     * Imposta il {@link UtenteRegistrato} come leader del team.
     *
     * @param leader l'utente che diventerà leader; non può essere null
     * @throws IllegalArgumentException se il leader è null
     * @throws IllegalStateException se l'utente non è già membro del team
     *
     * @author Giulia Trozzi
     */
    public void setTeamLeader(UtenteRegistrato leader) {
        if (leader == null)
            throw new IllegalArgumentException("Leader non valido.");

        if (!teamMembersList.contains(leader))
            throw new IllegalStateException("Il leader deve essere membro del team.");

        this.teamLeader = leader;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}