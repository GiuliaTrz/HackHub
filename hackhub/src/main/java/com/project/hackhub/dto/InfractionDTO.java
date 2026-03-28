package com.project.hackhub.dto;

import com.project.hackhub.model.team.Team;

public record InfractionDTO (String description, String type, Team team){ }
