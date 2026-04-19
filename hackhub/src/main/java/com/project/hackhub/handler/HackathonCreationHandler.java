package com.project.hackhub.handler;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.FileTemplate;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Task;
import com.project.hackhub.model.hackathon.builder.Director;
import com.project.hackhub.model.hackathon.builder.HackathonBuilder;
import com.project.hackhub.model.hackathon.builder.HackathonBuilderMemento;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.*;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static com.project.hackhub.service.UserStateService.changeUserState;

@AllArgsConstructor
public class HackathonCreationHandler {

    private final HackathonRepository hackathonRepository;
    private final TaskRepository taskRepository;
    private final HackathonRepository hackathonRepo;
    private final HackathonBuilderMementoRepository hackathonBuilderMementoRepo;
    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteRegistratoRepository userRepository;

    public void insertTask(String title, String description, FileTemplate f, UUID hackathonId){
        Hackathon h = hackathonRepository.findById(hackathonId).orElseThrow(() -> new IllegalArgumentException("Hackathon can't null"));
        Task t = new Task(title, description, f);
        this.taskRepository.save(t);
        h.addTask(t);
        this.hackathonRepository.save(h);
    }

    public HackathonBuilder createHackathonBuilder(){
        return new HackathonBuilder();
    }

    /**
     * Checks if a reservation is available
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
    /**
     * Creates a Hackathon or a HackathonBuilderMemento if the dto given is not complete.
     * At the start of the creation restores a memento if the coordinator that is trying to create it
     * already has a suspended creation.
     *
     * @param dto the list of attributes needed to create a Hackathon
     * @throws IllegalArgumentException if the dto given is {@code null}
     *
     * @author Giorgia Branchesi
     */
    public void createHackathon(HackathonDTO dto, UUID coordinator) {

        if(dto == null) throw new IllegalArgumentException("HackathonDTO cannot be null");

        UtenteRegistrato coordinatorU = userRepository.findById(coordinator).orElseThrow(
                () -> new IllegalArgumentException("coordinator cannot be null"));

        HackathonBuilder hackathonBuilder = createHackathonBuilder();
        hackathonBuilderMementoRepo.findByAuthor(coordinatorU).ifPresent(hackathonBuilder::restoreMemento);

        populateBuilder(dto, hackathonBuilder);
        if(hackathonBuilder.isComplete()) {
            hackathonBuilder.setState();
            hackathonBuilder.setCoordinator(coordinatorU);
            Hackathon hackathon = hackathonBuilder.getProduct();
            updateStaffState(hackathon);
            hackathonRepo.save(hackathon);

            hackathonBuilderMementoRepo.removeHackathonBuilderMementoByAuthor(coordinatorU);
        }
        else
        {
            HackathonBuilderMemento memento =  hackathonBuilder.saveMemento();
            hackathonBuilderMementoRepo.save(memento);
        }
    }

    /**
     * Updates the state of the staff assigned to a Hackathon
     *
     * @param hackathon the hackathon
     * @author Giorgia Branchesi
     */
    private void updateStaffState(Hackathon hackathon) {
        changeUserState(hackathon.getJudge(), true, hackathon, UserStateType.GIUDICE);
        changeUserState(hackathon.getCoordinator(), true, hackathon, UserStateType.ORGANIZZATORE);
        if(hackathon.getMentorsList() != null) {
            for (UtenteRegistrato mentor : hackathon.getMentorsList()) {
                changeUserState(mentor, true, hackathon, UserStateType.MENTORE);
            }
        }
    }

    /**
     * Populates the builder thanks to the {@link Director} class.
     *
     * @param dto the information needed to populate the builder
     * @param builder the builder to use
     *
     * @author Giorgia Branchesi
     */
    private void populateBuilder(HackathonDTO dto, HackathonBuilder builder){

        Director director = new Director(builder, this);
        director.populateBuilder(dto);
    }
}
