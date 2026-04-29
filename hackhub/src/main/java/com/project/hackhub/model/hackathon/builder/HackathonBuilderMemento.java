package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.user.User;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Concrete implementation of the Memento design pattern for {@link HackathonBuilder}.

 * This class captures and stores the state of a {@link HackathonBuilder} at a specific point in time
 * by wrapping a {@link HackathonSnapshot}. The snapshot stores only essential data (strings, dates, UUIDs)
 * without maintaining full entity references, allowing for lightweight persistence without creating
 * incomplete {@link Hackathon} entities in the database.
 * The memento enables the following functionality:
 */
public class HackathonBuilderMemento implements Memento {

    @Getter
    private final HackathonSnapshot snapshot;

    /**
     * Constructs a new HackathonBuilderMemento wrapping the given snapshot.
     *
     * @param snapshot the snapshot containing the saved state; must not be null
     */
    public HackathonBuilderMemento(HackathonSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * Factory method that creates a HackathonBuilderMemento from the current builder state.
     * This method extracts all relevant data from the builder's internal {@link Hackathon} object
     * and stores it in a new {@link HackathonSnapshot}. Associated users (mentors, judge, coordinator)
     * are converted to their UUID representations to:
     * <ul>
     *   <li>Decouple the snapshot from entity relationships</li>
     *   <li>Allow lightweight persistence without incomplete Hackathon entities in the database</li>
     *   <li>Simplify snapshot serialization and storage</li>
     * </ul>
     *
     * @param builder the builder containing the hackathon state to capture; must not be null
     * @param author the user creating this memento, used for eventual retrieval; must not be null
     * @return a new HackathonBuilderMemento wrapping the created snapshot
     *
     * @throws NullPointerException if builder or author is null
     */
    public static HackathonBuilderMemento fromBuilder(
            HackathonBuilder builder,
            User author
    ) {

        Hackathon hackathon = builder.getProduct();

        HackathonSnapshot snapshot = new HackathonSnapshot();

        snapshot.setAuthor(author);

        snapshot.setName(hackathon.getName());
        snapshot.setRuleBook(hackathon.getRuleBook());
        snapshot.setExpiredSubscriptionsDate(hackathon.getExpiredSubscriptionsDate());
        snapshot.setMaxTeamDimension(hackathon.getMaxTeamDimension());
        snapshot.setMoneyPrice(hackathon.getMoneyPrice());
        snapshot.setReservation(hackathon.getReservation());

        // Always save mentors list, even if empty or null
        if (hackathon.getMentorsList() != null) {
            snapshot.setMentorsList(
                    new ArrayList<>(
                            hackathon.getMentorsList()
                                    .stream()
                                    .map(User::getId)
                                    .toList()
                    )
            );
        } else {
            snapshot.setMentorsList(new ArrayList<>());
        }
        if (hackathon.getJudge() != null) {
            snapshot.setJudge(hackathon.getJudge().getId());
        }
        return new HackathonBuilderMemento(snapshot);
    }

    /**
     * Restores the snapshot state into the given builder.
     * This method transfers all non-null fields from the snapshot back into the builder's
     * internal {@link Hackathon} object. It performs the following:
     * <ul>
     *   <li>Ensures the builder has a valid Hackathon instance by calling reset() if needed</li>
     *   <li>Restores all primitive fields (name, ruleBook, dates, dimensions, pricing)</li>
     *   <li>Restores the reservation object with cascade preservation</li>
     *   <li>Uses null checks to avoid overwriting data with null values</li>
     * </ul>
     *
     * <strong>Note:</strong> Associated users (mentors, judge, coordinator) are stored as UUIDs
     * in the snapshot and must be converted back to entity objects by the calling handler via
     * {@code restoreAssociatedUsers()}. This separation of concerns keeps the memento decoupled
     * from the repository layer and maintains architectural clarity.
     *
     * @param builder the builder whose internal state will be populated from the snapshot; must not be null
     *
     * @throws NullPointerException if builder is null
     */
    public void restoreInto(HackathonBuilder builder) {

        Hackathon hackathon = builder.getProduct();

        if (hackathon == null) {
            builder.reset();
            hackathon = builder.getProduct();
        }

        if (snapshot.getName() != null)
            hackathon.setName(snapshot.getName());

        if (snapshot.getRuleBook() != null)
            hackathon.setRuleBook(snapshot.getRuleBook());

        if (snapshot.getExpiredSubscriptionsDate() != null)
            hackathon.setExpiredSubscriptionsDate(snapshot.getExpiredSubscriptionsDate());

        if (snapshot.getMaxTeamDimension() != null)
            hackathon.setMaxTeamDimension(snapshot.getMaxTeamDimension());

        if (snapshot.getMoneyPrice() != null)
            hackathon.setMoneyPrice(snapshot.getMoneyPrice());

        if (snapshot.getReservation() != null)
            hackathon.setReservation(snapshot.getReservation());
    }

    /**
      * Returns the snapshot representing the saved state of this memento.
      * The snapshot contains all essential data needed to reconstruct the builder state,
      * including the author identifier for lookup and all hackathon creation parameters.
      *
      * @return the {@link HackathonSnapshot} containing the memento state
      */
     @Override
     public HackathonSnapshot getState() {
         return snapshot;
     }
 }
