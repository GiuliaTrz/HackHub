package com.project.hackhub.dto;

import com.project.hackhub.model.team.FileTemplate;

public record TaskDTO(
        String title,
        String description,
        FileTemplate template
) {
}
