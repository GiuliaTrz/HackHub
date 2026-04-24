package com.project.hackhub.handler;

import com.project.hackhub.dto.HackathonCreationResponse;
import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.dto.TaskDTO;
import com.project.hackhub.model.hackathon.state.HackathonState;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.FileTemplate;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Task;
import com.project.hackhub.model.hackathon.builder.Director;
import com.project.hackhub.model.hackathon.builder.HackathonBuilder;
import com.project.hackhub.model.hackathon.builder.HackathonBuilderMemento;
import com.project.hackhub.model.hackathon.builder.HackathonSnapshot;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.*;
import com.project.hackhub.service.UserStateService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.project.hackhub.model.utente.state.Permission.CAN_ADD_TASK;

@Component
@AllArgsConstructor
public class HackathonCreationHandler {

    private final HackathonRepository hackathonRepository;
    private final TaskRepository taskRepository;
    private final HackathonRepository hackathonRepo;
    private final HackathonSnapshotRepository snapshotRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteRegistratoRepository userRepository;
    private final UserStateService userStateService;

    @Transactional
    public void insertTask(UUID coordinator, TaskDTO taskDTO, UUID hackathonId) {

        UtenteRegistrato c = userRepository.findById(coordinator)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator can't null"));

        Hackathon h = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon can't null"));

        if(!c.hasPermission(CAN_ADD_TASK, h) || !h.getState().getStateType().equals(HackathonStateType.IN_ISCRIZIONE))
            throw new UnsupportedOperationException("User doesn't have permission to add task or hackathon is not in iscrizione state");

        Task t = new Task(taskDTO.title(), taskDTO.description(), taskDTO.template());
        this.taskRepository.save(t);
        h.addTask(t);
        this.hackathonRepo.save(h);
    }

    public HackathonBuilder createHackathonBuilder() {
        return new HackathonBuilder();
    }

    /**
     * Checks if a reservation is available
     *
     * @param reservation the reservation
     * @return true if it is, false if not
     * @author Giorgia Branchesi
     * @author Giulia Trozzi
     */
    public boolean isReservationAvailable(Prenotazione reservation) {

        if (reservation == null)
            return false;

        if (reservation.getLocation() == null || reservation.getTimeInterval() == null)
            return false;

        return !prenotazioneRepository.existsByLocationAndTimeInterval(
                reservation.getLocation(),
                reservation.getTimeInterval()
        );
    }

    @Transactional
    public HackathonCreationResponse createHackathon(
            HackathonDTO dto,
            UUID coordinatorId
    ) {
        // Input validation
        if (dto == null) {
            throw new IllegalArgumentException("HackathonDTO cannot be null");
        }

        // Get coordinator
        UtenteRegistrato coordinator = userRepository.findById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));

        // Initialize builder
        HackathonBuilder builder = new HackathonBuilder();
        builder.reset();

        // Try to restore existing snapshot
        Optional<HackathonSnapshot> existingSnapshot = snapshotRepository.findByAuthor(coordinator);

        // Restore state from snapshot if present
        existingSnapshot.ifPresent(snapshot -> {
            builder.restoreFromSnapshot(snapshot, userRepository);
        });

        // Apply new data from DTO
        Director director = new Director(builder, userRepository);
        director.populateBuilder(dto);

        // Check if hackathon is complete
        if (builder.isComplete()) {
            return completeHackathonCreation(builder, coordinator, existingSnapshot);
        } else {
            return suspendHackathonCreation(builder, coordinator, existingSnapshot);
        }
    }

    private HackathonCreationResponse completeHackathonCreation(
            HackathonBuilder builder,
            UtenteRegistrato coordinator,
            Optional<HackathonSnapshot> existingSnapshot
    ) {
        // Set final properties
        builder.setCoordinator(coordinator);
        builder.setState();

        // Save hackathon
        Hackathon hackathon = builder.getProduct();
        prenotazioneRepository.save(hackathon.getReservation());
        hackathonRepo.save(hackathon);

        // Update staff states
        updateStaffState(hackathon);

        // Clean up snapshot
        existingSnapshot.ifPresent(snapshot -> snapshotRepository.delete(snapshot));

        return new HackathonCreationResponse(true, "Hackathon created successfully");
    }

    private HackathonCreationResponse suspendHackathonCreation(
            HackathonBuilder builder,
            UtenteRegistrato coordinator,
            Optional<HackathonSnapshot> existingSnapshot
    ) {
        // Create or update snapshot
        HackathonSnapshot snapshot = existingSnapshot.orElse(new HackathonSnapshot());
        snapshot.setAuthor(coordinator);

        // Save current builder state to snapshot
        HackathonBuilderMemento memento = builder.saveMemento(coordinator);
        HackathonSnapshot currentState = memento.getSnapshot();

        // Merge with existing data if present
        if (existingSnapshot.isPresent()) {
            snapshot.setName(currentState.getName() != null ? currentState.getName() : snapshot.getName());
            snapshot.setRuleBook(currentState.getRuleBook() != null ? currentState.getRuleBook() : snapshot.getRuleBook());
            snapshot.setExpiredSubscriptionsDate(currentState.getExpiredSubscriptionsDate() != null ?
                currentState.getExpiredSubscriptionsDate() : snapshot.getExpiredSubscriptionsDate());
            snapshot.setMaxTeamDimension(currentState.getMaxTeamDimension() != null ?
                currentState.getMaxTeamDimension() : snapshot.getMaxTeamDimension());
            snapshot.setMoneyPrice(currentState.getMoneyPrice() != null ? currentState.getMoneyPrice() : snapshot.getMoneyPrice());
            snapshot.setReservation(currentState.getReservation() != null ? currentState.getReservation() : snapshot.getReservation());
            snapshot.setJudge(currentState.getJudge() != null ? currentState.getJudge() : snapshot.getJudge());
            snapshot.setMentorsList(currentState.getMentorsList() != null ? currentState.getMentorsList() : snapshot.getMentorsList());
        } else {
            // First time saving
            snapshot.setName(currentState.getName());
            snapshot.setRuleBook(currentState.getRuleBook());
            snapshot.setExpiredSubscriptionsDate(currentState.getExpiredSubscriptionsDate());
            snapshot.setMaxTeamDimension(currentState.getMaxTeamDimension());
            snapshot.setMoneyPrice(currentState.getMoneyPrice());
            snapshot.setReservation(currentState.getReservation());
            snapshot.setJudge(currentState.getJudge());
            snapshot.setMentorsList(currentState.getMentorsList());
        }

        snapshotRepository.save(snapshot);

        return new HackathonCreationResponse(false, "Hackathon creation suspended, missing information");
    }

    private void updateStaffState(Hackathon hackathon) {
        userStateService.changeUserState(hackathon.getJudge(), true, hackathon, UserStateType.GIUDICE);
        userStateService.changeUserState(hackathon.getCoordinator(), true, hackathon, UserStateType.ORGANIZZATORE);

        if (hackathon.getMentorsList() != null) {
            for (UtenteRegistrato mentor : hackathon.getMentorsList()) {
                userStateService.changeUserState(mentor, true, hackathon, UserStateType.MENTORE);
            }
        }
    }
}
