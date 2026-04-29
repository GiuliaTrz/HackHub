package com.project.hackhub.model.team;

import com.project.hackhub.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Invitation {

    @ManyToOne
    private Team sender;

    @ManyToOne
    @JoinColumn(name = "adressee_id")
    private User addressee;

    @Id
    @GeneratedValue
    private UUID id;

    boolean pending;

    public Invitation(Team team, User user) {
        if (team == null || user == null)
            throw new IllegalArgumentException("Invalid parameters.");

        this.sender = team;
        this.addressee = user;
        this.pending = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invitation other = (Invitation) o;

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}