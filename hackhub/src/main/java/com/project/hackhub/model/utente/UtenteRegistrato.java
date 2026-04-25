package com.project.hackhub.model.utente;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.utente.state.DefaultState;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.model.utente.state.UserState;
import com.project.hackhub.model.utente.state.UserStateFactory;
import com.project.hackhub.model.utente.state.UserStateType;
import lombok.*;
import jakarta.persistence.*;
import java.util.*;

@NoArgsConstructor
@Entity
public class UtenteRegistrato {

    @Id @GeneratedValue
    @Getter private UUID id;

    @OneToMany(mappedBy = "destinatario",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Invito> invitationsList = new LinkedHashSet<>();

    @Embedded
    @Setter @NonNull private Anagrafica anagrafica;

    @Getter private String passwordHash;

    @Getter @Setter private boolean organizer = false;

    @Transient
    private final UserStateFactory factory = new UserStateFactory();

    @ElementCollection
    @CollectionTable(name = "state_in_hackathon",
            joinColumns = @JoinColumn(name = "utente_registrato_id"))
    @MapKeyJoinColumn(name = "id")
    private Map<Prenotazione, UserStateType> stateInHackathon = new HashMap<>();

    public UtenteRegistrato(@NonNull Anagrafica a, String passwordHash){
        this.anagrafica = a;
        this.passwordHash = passwordHash;
    }

    public UserState getState(Hackathon hackathon){
        if (hackathon == null) return new DefaultState();
        UserStateType type = stateInHackathon.get(hackathon.getReservation());
        if (type == null) return new DefaultState();
        return factory.createUserState(type);
    }

    public void setState(Prenotazione prenotazione, UserStateType newStateType){
        this.stateInHackathon.put(prenotazione, newStateType);
    }

    public void removeReservation(Prenotazione p){

        if(p == null)
            throw new IllegalArgumentException("Reservation can't be null");
        stateInHackathon.remove(p);
    }

    public boolean isAvailable(Prenotazione p){
        if(p == null)
            throw new IllegalArgumentException("Reservation can't be null");
        for(Prenotazione prenotazione : stateInHackathon.keySet()){
            if(prenotazione.overlapsWith(p))
                return false;}
        return true;
    }

    public void addInvitation(Invito i){
        if (i == null)
            throw new IllegalArgumentException("Can't add empty invitation");
        if(this.invitationsList.contains(i))
            throw new IllegalArgumentException("Can't duplicate invitation");
        this.invitationsList.add(i);
    }
    public void removeInvitation(Invito i){
        if(i == null)
            throw new IllegalArgumentException("Invitation can't be null");
        if(!invitationsList.contains(i))
            throw new NoSuchElementException("Invitation not contained in invitationList");
        invitationsList.remove(i);
    }

    public boolean hasPermission(Permission p, Hackathon h){
        return this.getState(h).hasPermission(p);
}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UtenteRegistrato that = (UtenteRegistrato) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
