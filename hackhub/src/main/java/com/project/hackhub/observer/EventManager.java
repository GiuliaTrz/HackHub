package com.project.hackhub.observer;

import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public final class EventManager {

    public <T> void notify(EventType e, List<UtenteRegistrato> users, T entity) {


    }
}
