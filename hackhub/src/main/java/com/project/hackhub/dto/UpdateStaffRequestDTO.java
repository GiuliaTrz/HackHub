package com.project.hackhub.dto;

import java.util.UUID;

public record UpdateStaffRequestDTO(UUID toChange, String role) {

}
