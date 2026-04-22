package com.project.hackhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotBlank String userName,
        @Size(min = 8) String password
) {
}
