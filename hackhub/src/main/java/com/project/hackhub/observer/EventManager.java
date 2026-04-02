package com.project.hackhub.observer;

import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class EventManager {

    private List<EventListener> listeners = new ArrayList<>();

    public <T> void notify(EventType e, List<UtenteRegistrato> users, T entity) {

        if (users.isEmpty()) return;
        if (e == null)
            throw new IllegalArgumentException("event type cannot be null");
        if (entity == null)
            throw new IllegalArgumentException("entity cannot be null");

        if (!e.getEntityClass().isInstance(entity)) {
            throw new IllegalArgumentException("Entity type mismatch");
        }

        for (EventListener el : listeners) {
            if (el.getSupportedEventType().equals(e)) {
                el.updateUsers(users, "Hai ricevuto un'invito a partecipare ad un Team!", entity);
            }
        }
    }

    public void addListenerToList(EventListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener to add cannot be null");

        listeners.add(listener);
    }
}
