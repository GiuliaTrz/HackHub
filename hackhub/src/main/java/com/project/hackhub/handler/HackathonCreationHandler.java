package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.FileTemplate;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Task;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TaskRepository;

import java.util.UUID;

public class HackathonCreationHandler {
    private final HackathonRepository hackathonRepository;
    private final TaskRepository taskRepository;

    public HackathonCreationHandler(HackathonRepository hackathonRepository, TaskRepository taskRepository) {
        this.hackathonRepository = hackathonRepository;
        this.taskRepository = taskRepository;
    }

    public void insertTask(String title, String description, FileTemplate f, UUID hackathonId){
        Hackathon h = hackathonRepository.findById(hackathonId).orElseThrow(() -> new IllegalArgumentException("Hackathon can't null"));
        Task t = new Task(title, description, f);
        this.taskRepository.save(t);
        h.addTask(t);
        this.hackathonRepository.save(h);
    }
}
