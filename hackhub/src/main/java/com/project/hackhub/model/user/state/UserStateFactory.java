package com.project.hackhub.model.user.state;


public class UserStateFactory {

    public UserState createUserState(UserStateType st) {
        return switch (st) {
            case COORDINATOR -> new Coordinator();
            case MENTOR -> new Mentor();
            case JUDGE -> new Judge();
            case TEAM_MEMBER -> new TeamMember();
            case TEAM_LEADER -> new TeamLeader();
            case DEFAULT_STATE -> new DefaultState();
        };
    }
}
