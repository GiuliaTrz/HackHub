package com.project.hackhub.model.hackathon.state;

public class HackathonStateFactory {

    public HackathonState createState(HackathonStateType st){

        return switch(st){
            case IN_ISCRIZIONE -> new InIscrizione();
            case IN_CORSO -> new InCorso();
            case IN_VALUTAZIONE -> new InValutazione();
            case CONCLUSO -> new Concluso();
        };
    };


}
