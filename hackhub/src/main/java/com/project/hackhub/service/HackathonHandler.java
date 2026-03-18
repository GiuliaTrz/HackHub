package com.project.hackhub.service;

import com.project.hackhub.dto.HackathonDTO;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.builder.HackathonBuilder;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.HackathonBuilderMementoRepository;
import com.project.hackhub.repository.HackathonRepository;

public class HackathonHandler {

    private HackathonRepository hackathonRepo;

    private HackathonBuilder hackathonBuilder;

    private HackathonBuilderMementoRepository hackathonBuilderMementoRepo;


    HackathonHandler(HackathonBuilder b, HackathonRepository h, HackathonBuilderMementoRepository hm){

    }

    public boolean saveMemento(){
        return true;
    }

    public boolean addMentor(UtenteRegistrato u, Hackathon h){

        return true;
    }

    public void handleExpiredSubscriptions(){

    }

    public boolean removeTeamFromHackathon(Hackathon h, Team t){
        return true;
    }

    public void handleExpiredSubmissions(){

    }

    public HackathonBuilder createHackathonBuilder(){
        return null;
    }

    public void insertData(HackathonDTO dto){

    }

    public boolean checkHackathonDTO(HackathonDTO dto){
        return true;
    }

    public boolean deleteHackathon(Hackathon h){
        return true;
    }
}
