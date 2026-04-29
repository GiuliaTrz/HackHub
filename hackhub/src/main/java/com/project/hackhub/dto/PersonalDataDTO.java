package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Location;

public record PersonalDataDTO(
        String userName,
        String userSurname,
        String fiscalCode,
        Location address,
        String email,
        String password
){ }
