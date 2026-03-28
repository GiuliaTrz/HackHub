package com.project.hackhub.model.utente;

import com.project.hackhub.model.hackathon.Localita;
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
public class Anagrafica {

    private String userName;
    private String userSurname;
    private String fiscalCode;
    private Localita address;
    private String email;

}
