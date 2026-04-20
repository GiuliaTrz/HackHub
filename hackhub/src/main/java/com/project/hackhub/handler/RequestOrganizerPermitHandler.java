package com.project.hackhub.handler;

import com.project.hackhub.model.team.FileTemplate;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class RequestOrganizerPermitHandler {

    private final UtenteRegistratoRepository userRepository;

    /**
     * Handles the request of a user to get a permit to organize an Hackathon
     * @param user the user that's requesting the permit
     * @param f the fileTemplate loaded
     * @throws IllegalArgumentException if the user is null or if the fileTemplate is not valid
     * @author Giorgia Branchesi
     */
    public void requestPermission(UUID user, FileTemplate f) {

        UtenteRegistrato user1 = userRepository.findById(user).orElseThrow(
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

        //file simulato, validità simulata
        return true;
    }

}
