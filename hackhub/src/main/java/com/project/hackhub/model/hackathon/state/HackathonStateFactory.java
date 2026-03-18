package com.project.hackhub.model.hackathon.state;

public class HackathonStateFactory {

    public HackathonState createState(){
        return new InIscrizione();
    }

    public HackathonState createState(HackathonStateType st){
        return switch(st){
            case IN_ISCRIZIONE -> new InCorso();
            case IN_CORSO -> new InValutazione();
            case IN_VALUTAZIONE -> new Concluso();
            default -> throw new IllegalArgumentException("Invalid StateType");
        };
    };


}
