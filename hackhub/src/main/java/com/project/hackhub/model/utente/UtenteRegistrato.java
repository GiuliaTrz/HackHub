package com.project.hackhub.model.utente;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.team.Invito;
import com.project.hackhub.model.utente.state.DefaultState;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.model.utente.state.UserState;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UtenteRegistrato implements Utente {

    @Id @GeneratedValue
    @Getter private final UUID id = UUID.randomUUID();;

    @OneToMany
    private Set<Invito> invitationsList = new LinkedHashSet<>();

    @Embedded
    @Setter(AccessLevel.PROTECTED) @NonNull private Anagrafica anagrafica;

    @Embedded
    private final UserState defaultState = new DefaultState();

    @ElementCollection @CollectionTable(name = "StateInHackathon")
    private Map<Prenotazione, UserState> stateInHackathon = new HashMap<>();

    @OneToMany //to remove
    private Set<Prenotazione> reservationsList = stateInHackathon.keySet();

    public UtenteRegistrato(@NonNull Anagrafica a){
        this.anagrafica = a;
    }

    public UserState getState(Hackathon hackathon){
        if (hackathon == null) return defaultState;
        return stateInHackathon.getOrDefault(hackathon, new DefaultState());
    }

    public void setState(Prenotazione prenotazione, UserState newState){
        this.stateInHackathon.put(prenotazione, newState);
    }

    public void addReservation(Hackathon h){
        if(h == null)
            throw new IllegalArgumentException("Hackathon can't be null");
        if (isAvailable(h.getReservation())) {
            reservationsList.add(h.getReservation());
        }
    }

    public void removeReservation(Hackathon h){
        if(h == null)
            throw new IllegalArgumentException("Hackathon can't be null");
        if(!reservationsList.contains(h.getReservation()))
            throw new NoSuchElementException("Reservation not contained in reservationList");
        reservationsList.remove(h.getReservation());
    }

    public boolean isAvailable(Prenotazione p){
        if(p == null)
            throw new IllegalArgumentException("Reservation can't be null");
        return (!reservationsList.contains(p));
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

}
