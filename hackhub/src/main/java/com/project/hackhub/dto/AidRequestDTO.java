package com.project.hackhub.dto;

import com.project.hackhub.model.team.AidRequestType;
import com.project.hackhub.model.team.Team;

public record AidRequestDTO(String description, AidRequestType type, Team team) {
}
