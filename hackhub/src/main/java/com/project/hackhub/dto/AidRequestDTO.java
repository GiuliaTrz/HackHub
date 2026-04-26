package com.project.hackhub.dto;

import com.project.hackhub.model.team.AidRequestType;
import java.util.UUID;

public record AidRequestDTO(String description, AidRequestType type, UUID team) {
}
