package com.project.hackhub.model.user;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Reservation;
import com.project.hackhub.model.team.Invitation;
import com.project.hackhub.model.user.state.DefaultState;
import com.project.hackhub.model.user.state.Permission;
import com.project.hackhub.model.user.state.UserState;
import com.project.hackhub.model.user.state.UserStateFactory;
import com.project.hackhub.model.user.state.UserStateType;
import lombok.*;
import jakarta.persistence.*;
import java.util.*;

@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    @Getter private UUID id;

    @OneToMany(mappedBy = "addressee")
    @Getter private Set<Invitation> invitationsList = new LinkedHashSet<>();

    @Embedded
    @Setter @Getter @NonNull private PersonalData personalData;

    @Getter private String passwordHash;

    @Getter @Setter private boolean organizer = false;

    @Transient
    private final UserStateFactory factory = new UserStateFactory();

    @ElementCollection
    @CollectionTable(name = "state_in_hackathon",
            joinColumns = @JoinColumn(name = "utente_registrato_id"))
    @MapKeyJoinColumn(name = "id")
    private Map<Reservation, UserStateType> stateInHackathon = new HashMap<>();

    public User(@NonNull PersonalData a, String passwordHash){
        this.personalData = a;
        this.passwordHash = passwordHash;
    }

    public UserState getState(Hackathon hackathon){
        if (hackathon == null) return new DefaultState();
        UserStateType type = stateInHackathon.get(hackathon.getReservation());
        if (type == null) return new DefaultState();
        return factory.createUserState(type);
    }

    public void setState(Reservation reservation, UserStateType newStateType){

        if(stateInHackathon.containsKey(reservation))
            return;

        this.stateInHackathon.put(reservation, newStateType);
    }

    public void removeReservation(Reservation p){

        if(p == null)
            throw new IllegalArgumentException("Reservation can't be null");
        stateInHackathon.remove(p);
    }

    public boolean isAvailable(Reservation p){
        if(p == null)
            throw new IllegalArgumentException("Reservation can't be null");
        for(Reservation reservation : stateInHackathon.keySet()){
            if(reservation.overlapsWith(p))
                return false;}
        return true;
    }

    public void addInvitation(Invitation i){
        if (i == null)
            throw new IllegalArgumentException("Can't add empty invitation");
        if(this.invitationsList.contains(i))
            throw new IllegalArgumentException("Can't duplicate invitation");
        this.invitationsList.add(i);
    }
    public void removeInvitation(Invitation i){
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
        User that = (User) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
