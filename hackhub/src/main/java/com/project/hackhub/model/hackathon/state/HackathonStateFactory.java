package com.project.hackhub.model.hackathon.state;

public class HackathonStateFactory {

    public HackathonState createState(HackathonStateType st){

        return switch(st){
            case SUBSCRIPTION_PHASE -> new SubscriptionTime();
            case ONGOING -> new OnGoing();
            case APPRAISAL -> new Appraisal();
            case CONCLUDED -> new Concluded();
        };
    };


}
