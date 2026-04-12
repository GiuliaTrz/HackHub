package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.FileTemplate;
import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Task;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.TaskRepository;

public class HackathonCreationHandler {
    private final HackathonRepository hackathonRepository;
    private final TaskRepository taskRepository;

    public HackathonCreationHandler(HackathonRepository hackathonRepository, TaskRepository taskRepository) {
        this.hackathonRepository = hackathonRepository;
        this.taskRepository = taskRepository;
    }

    public void insertTask(String title, String description, FileTemplate f, Hackathon h){
        Task t = new Task(title, description, f);
        this.taskRepository.save(t);
        h.addTask(t);
        this.hackathonRepository.save(h);
    }
}
