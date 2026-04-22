package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Localita;

public record AnagraficaDTO (
        String userName,
        String userSurname,
        String fiscalCode,
        Localita address,
        String email,
        String password
){ }
