package com.project.hackhub.model.user.state;

import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@NoArgsConstructor
public abstract class UserState {

    protected Set<Permission> permissions;

    protected UserState(Set<Permission> permissions) {
        this.permissions = (permissions == null)? Collections.emptySet() : permissions;
    }

    public abstract UserStateType getType();

    public boolean hasPermission(Permission p) {
        return permissions.contains(p);
    }

}
