package com.project.hackhub.model.utente;

import com.project.hackhub.model.hackathon.Localita;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Anagrafica {

    private String name;
    private String surname;
    private String fiscalCode;
    private Localita address;
    private String email;

}
