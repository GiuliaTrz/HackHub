package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.team.Team;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Submission {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne
    private Task task;
    @ManyToOne
    private Team team;
    @Embedded
    private FileTemplate fileTemplate;

    private LocalDateTime timestamp = LocalDateTime.now();

    private float grade;
}
