package com.project.hackhub.dto;

import java.util.UUID;

public record SubmissionDTO(UUID taskId,
        UUID teamId,
        String fileName) {}

