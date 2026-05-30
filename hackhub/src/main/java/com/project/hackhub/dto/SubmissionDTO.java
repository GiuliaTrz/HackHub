package com.project.hackhub.dto;

import java.util.UUID;

public record SubmissionDTO(
        UUID teamId,
        String fileName) {}

