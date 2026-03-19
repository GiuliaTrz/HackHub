package com.project.hackhub.service;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.builder.Director;
import com.project.hackhub.model.hackathon.builder.HackathonBuilder;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.HackathonBuilderMementoRepository;
import com.project.hackhub.repository.HackathonRepository;

public class HackathonHandler {

    private final HackathonRepository hackathonRepo;

    private final HackathonBuilder hackathonBuilder;

    private final HackathonBuilderMementoRepository hackathonBuilderMementoRepo;


    public HackathonHandler(HackathonBuilder b, HackathonRepository h, HackathonBuilderMementoRepository hm){

        this.hackathonRepo = h;
        this.hackathonBuilder = b;
        this.hackathonBuilderMementoRepo = hm;
    }

    public boolean saveMemento(){

        //TODO
        return true;
    }

    public void addMentor(UtenteRegistrato u, Hackathon h){
        h.addMentor(u);
        hackathonRepo.save(h);
    }

    public void handleExpiredSubscriptions(){
          //TODO
    }

    public boolean removeTeamFromHackathon(Hackathon h, Team t){

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

    public void insertData(HackathonDTO dto){
        Director director = new Director(hackathonBuilder);
        director.populateBuilder(dto);
    }

    public boolean checkHackathonDTO(HackathonDTO dto){
        //TODO
        return true;
    }

    public boolean deleteHackathon(Hackathon h){

        //TOREVISE
        if(h == null) throw new IllegalArgumentException("Hackathon to delete cannot be null");

        hackathonRepo.delete(h);
        return true;
    }
}
