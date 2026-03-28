package com.project.hackhub.service;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.builder.Director;
import com.project.hackhub.model.hackathon.builder.HackathonBuilder;
import com.project.hackhub.model.hackathon.builder.HackathonBuilderMemento;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.model.utente.state.Permission;
import com.project.hackhub.repository.HackathonBuilderMementoRepository;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.PrenotazioneRepository;
import java.time.LocalDate;

public class HackathonHandler {

    private final HackathonRepository hackathonRepo;

    private final HackathonBuilder hackathonBuilder;

    private final HackathonBuilderMementoRepository hackathonBuilderMementoRepo;

    private final UtenteRegistratoHandler utenteRegistratoHandler;

    private final PrenotazioneRepository prenotazioneRepository;


    public HackathonHandler(HackathonBuilder b, HackathonRepository h, HackathonBuilderMementoRepository hm, UtenteRegistratoHandler uh, PrenotazioneRepository pr){

        this.hackathonRepo = h;
        this.hackathonBuilder = b;
        this.hackathonBuilderMementoRepo = hm;
        this.utenteRegistratoHandler = uh;
        this.prenotazioneRepository = pr;
    }

    public boolean saveMemento(){

        //TODO
        return true;
    }

    public void addMentor(UtenteRegistrato u, Hackathon h){

        if(h == null) throw new IllegalArgumentException("hacakthon cannot be null");
        if(u == null) throw new IllegalArgumentException("user cannot be null");
        if(!u.hasPermission(Permission.CAN_ADD_MENTOR, h))
            throw new UnsupportedOperationException("Azione non permessa.");
        h.addMentor(u);
        hackathonRepo.save(h);
    }

    public void handleExpiredSubscriptions(){
          //TODO
    }

    public boolean removeTeamFromHackathon(Hackathon h, Team t){
        if(!t.getTeamLeader().hasPermission(Permission.CAN_UNSUBSCRIBE_TEAM, h))
        throw new UnsupportedOperationException("Azione non permessa.");
        if(h == null || t == null) return false;

        if(h.removeTeam(t)){
            hackathonRepo.save(h);
            return true;
        }
        return false;
    }

    public void handleExpiredSubmissions(){
        //TODO
    }

    public HackathonBuilder createHackathonBuilder(){
        return new HackathonBuilder();
    }

    //TO REVISE
    public boolean checkHackathonDTO(HackathonDTO dto){

        if(dto == null) throw new IllegalArgumentException("dto cannot be null");
        if(dto.name() == null || dto.name().isBlank()) return false;
        if(dto.ruleBook() == null || dto.ruleBook().isBlank()) return false;
        if(dto.expiredSubscriptionsDate() == null
                || dto.expiredSubscriptionsDate().isBefore(LocalDate.now())
                || dto.expiredSubscriptionsDate().isBefore(LocalDate.now().plusDays(31))) return false;
        if(dto.maxTeamDimension() < 1 || dto.maxTeamDimension() > 20) return false;
        if(dto.moneyPrice() == null || dto.moneyPrice().getQuantity() < 0) return false;
        if(dto.coordinator() == null) return false;
        if(dto.judge() == null || !(utenteRegistratoHandler.checkAvailabilityUser(dto.judge()))) return false;
        if(dto.mentorsList() != null) {
            for(UtenteRegistrato mentor : dto.mentorsList()) {
                if(mentor == null || !(utenteRegistratoHandler.checkAvailabilityUser(mentor)))
                    return false;
            }
        }
        if(!isReservationAvailable(dto)) return false;

        return true;
    }

    private boolean isReservationAvailable(HackathonDTO dto) {
        Prenotazione p = dto.reservation();
        if(p == null) return false;
        if( p.getLocalita() == null || p.getIntervalloTemporale() == null) return false;
        return !prenotazioneRepository.existsByLocalitaAndIntervalloTemporale(p.getLocalita(), p.getIntervalloTemporale());
    }

    public boolean deleteHackathon(Hackathon h){

        //TOREVISE
        if(h == null) throw new IllegalArgumentException("Hackathon to delete cannot be null");

        hackathonRepo.delete(h);
        return true;
    }

    //added, to add anche alle classi di progetto
    public void creaHackathon(HackathonDTO dto) {

        if(dto == null) throw new IllegalArgumentException("HackathonDTO cannot be null");

        insertData(dto);
        if(checkHackathonDTO(dto)) {
            hackathonBuilder.setState();
            Hackathon hackathon = hackathonBuilder.getProduct();
            hackathonRepo.save(hackathon);
        }
        else
        {
            HackathonBuilderMemento state =  hackathonBuilder.saveMemento();
            hackathonBuilderMementoRepo.save(state);
        }
    }

    //changed to private
    private void insertData(HackathonDTO dto){

        if(dto == null) throw new IllegalArgumentException("dto cannot be null");

        Director director = new Director(hackathonBuilder);
        director.populateBuilder(dto);
    }
}
