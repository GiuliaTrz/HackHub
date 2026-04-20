package com.project.hackhub.model.hackathon;

import com.project.hackhub.model.team.FileTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Entity @NoArgsConstructor
@Setter
@Getter
public class Task {
    @Id @GeneratedValue
    private UUID id;
    private String description;
    private String title;

    public Task(String title, String description, FileTemplate f){
        this.title = title;
        this.description = description;
    }
}