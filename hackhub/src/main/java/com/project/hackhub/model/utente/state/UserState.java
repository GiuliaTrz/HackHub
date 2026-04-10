package com.project.hackhub.model.utente.state;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.team.Infraction;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Embeddable
public abstract class UserState {
    protected Set<Permission> permissions;


    protected UserState(Set<Permission> permissions) {
        this.permissions = (permissions == null)? Collections.emptySet() : permissions;
    }

    public boolean hasPermission(Permission p) {
        return permissions.contains(p);
    }

    public List<Infraction> getInfractions(Hackathon h){
        if(!permissions.contains(Permission.STAFF_PERMISSION))
            throw new UnsupportedOperationException("Azione non permessa.");
        return h.getInfractions();
    };

}
