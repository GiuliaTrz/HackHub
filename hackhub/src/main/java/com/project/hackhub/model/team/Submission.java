package com.project.hackhub.model.team;

import com.project.hackhub.model.hackathon.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Submission {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne
    private Task task;
    @ManyToOne
    private Team team;
    @Embedded
    private FileTemplate fileTemplate;

    private LocalDateTime timestamp;

    private Float grade;

    public Submission(Team team, Task task, FileTemplate fileTemplate){
        if (team == null || task == null || fileTemplate == null )
            throw new IllegalArgumentException("Invalid submission: can't have null arguments");
        this.team = team;
        this.task = task;
        this.fileTemplate = fileTemplate;
        this.timestamp = LocalDateTime.now();
    }
}
