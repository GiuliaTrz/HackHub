package com.project.hackhub.handler;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.builder.Director;
import com.project.hackhub.model.hackathon.builder.HackathonBuilder;
import com.project.hackhub.model.hackathon.builder.HackathonBuilderMemento;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.model.utente.state.UserStateType;
import com.project.hackhub.repository.HackathonBuilderMementoRepository;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.PrenotazioneRepository;

public class HackathonHandler {

    private final HackathonRepository hackathonRepo;

    private final HackathonBuilderMementoRepository hackathonBuilderMementoRepo;

    private final UtenteRegistratoHandler utenteRegistratoHandler;

    private final PrenotazioneRepository prenotazioneRepository;


    public HackathonHandler(HackathonRepository h, HackathonBuilderMementoRepository hm, UtenteRegistratoHandler uh, PrenotazioneRepository pr){

        this.hackathonRepo = h;
        this.hackathonBuilderMementoRepo = hm;
        this.utenteRegistratoHandler = uh;
        this.prenotazioneRepository = pr;
    }

    public void addMentor(UtenteRegistrato u, Hackathon h){

        if(h == null) throw new IllegalArgumentException("hacakthon cannot be null");
        if(u == null) throw new IllegalArgumentException("user cannot be null");
        if(!u.hasPermission(Permission.CAN_ADD_MENTOR, h))
            throw new UnsupportedOperationException("Azione non permessa.");
        h.addMentor(u);
        hackathonRepo.save(h);
    }

    public boolean removeTeamFromHackathon(Hackathon h, Team t){

        if(h == null || t == null) return false;

        if(!t.getTeamLeader().hasPermission(Permission.CAN_UNSUBSCRIBE_TEAM, h))
            throw new UnsupportedOperationException("Azione non permessa.");

        if(h.removeTeam(t)){
            hackathonRepo.save(h);
            return true;
        }
        return false;
    }

    public HackathonBuilder createHackathonBuilder(){
        return new HackathonBuilder();
    }

    /**
     *
     * @param dto
     * @return
     * @author Giorgia Branchesi
     * @author Giulia Trozzi
     */
    public boolean isReservationAvailable(HackathonDTO dto) {

        Prenotazione reservation = dto.reservation();

        if (reservation == null)
            return false;

        if (reservation.getLocation() == null || reservation.getTimeInterval() == null)
            return false;

        return !prenotazioneRepository.existsByLocationAndTimeInterval(
                reservation.getLocation(),
                reservation.getTimeInterval()
        );
    }

    public boolean deleteHackathon(Hackathon h){
        //TODO
        return false;
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
    public void createHackathon(HackathonDTO dto, UtenteRegistrato coordinator) {

        if(dto == null) throw new IllegalArgumentException("HackathonDTO cannot be null");
        if(coordinator == null) throw new IllegalArgumentException("coordinator cannot be null");

        HackathonBuilder hackathonBuilder = createHackathonBuilder();
        hackathonBuilderMementoRepo.findByAuthor(coordinator).ifPresent(hackathonBuilder::restoreMemento);

        populateBuilder(dto, hackathonBuilder);
        if(hackathonBuilder.isComplete()) {
            hackathonBuilder.setState();
            hackathonBuilder.setCoordinator(coordinator);
            Hackathon hackathon = hackathonBuilder.getProduct();
            updateStaffState(hackathon);
            hackathonRepo.save(hackathon);

            hackathonBuilderMementoRepo.removeHackathonBuilderMementoByAuthor(coordinator);
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
        utenteRegistratoHandler.changeUserState(hackathon.getJudge(), true, hackathon, UserStateType.GIUDICE);
        utenteRegistratoHandler.changeUserState(hackathon.getCoordinator(), true, hackathon, UserStateType.ORGANIZZATORE);
        if(hackathon.getMentorsList() != null) {
            for (UtenteRegistrato mentor : hackathon.getMentorsList()) {
                utenteRegistratoHandler.changeUserState(mentor, true, hackathon, UserStateType.MENTORE);
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
