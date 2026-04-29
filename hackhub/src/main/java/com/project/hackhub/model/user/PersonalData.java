package com.project.hackhub.model.user;

import com.project.hackhub.model.hackathon.Location;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PersonalData {

    private String userName;
    private String userSurname;
    private String fiscalCode;
    private Location address;
    private String email;

}
