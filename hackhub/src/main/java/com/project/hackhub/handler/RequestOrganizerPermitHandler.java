package com.project.hackhub.handler;

import com.project.hackhub.model.team.FileTemplate;
import com.project.hackhub.model.user.User;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RequestOrganizerPermitHandler {

    private final UserRepository userRepository;

    /**
     * Handles the request of a user to get a permit to organize a Hackathon
     * @param user the user that's requesting the permit
     * @param f the fileTemplate loaded
     * @throws IllegalArgumentException if the user is null or if the fileTemplate is not valid
     * @author Giorgia Branchesi
     */
    @Transactional
    public void requestPermission(UUID user, FileTemplate f) {

        User user1 = userRepository.findById(user).orElseThrow(
                () -> new IllegalArgumentException("user cannot be null")
        );

        if(checkTemplateValidity(f)) {
            user1.setOrganizer(true);
            userRepository.save(user1);
        }
        else
            throw new IllegalArgumentException("permit as organizer cannot be given");
    }

    private boolean checkTemplateValidity(FileTemplate f) {

        //simulated validity
        return true;
    }

}
