package com.project.hackhub.observer;

import com.project.hackhub.model.utente.UtenteRegistrato;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class EventManager {

    private final List<EventListener> listeners = new ArrayList<>();

    /**
     * Notifies a list of {@link UtenteRegistrato} about a specific event happening
     *
     * @param e      the type of event happening
     * @param users  the users to notify
     * @param entity the entity to use
     * @throws IllegalArgumentException if the event or the entity are null or if the entity
     *                                  is not supported by the type of event
     * @author Giorgia Branchesi
     */
    public void notify(EventType e, List<UtenteRegistrato> users, Object entity) {

        if (users == null || users.isEmpty()) return;
        if (e == null)
            throw new IllegalArgumentException("event type cannot be null");
        if (entity == null)
            throw new IllegalArgumentException("entity cannot be null");

        if (!e.getEntityClass().isInstance(entity)) {
            throw new IllegalArgumentException("Entity type mismatch");
        }

        for (EventListener el : listeners) {
            if (el.getSupportedEventType().equals(e)) {
                el.updateUsers(users,
                        "Hai ricevuto un'invito a partecipare ad un Team!",
                        entity);
            }
        }
    }

    /**
     * adds a listener to the list
     *
     * @param listener the listener
     * @throws IllegalArgumentException if the listener to add is {@code null}
     * @author Giorgia Branchesi
     */
    public void addListenerToList(EventListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener to add cannot be null");

        listeners.add(listener);
    }
}
