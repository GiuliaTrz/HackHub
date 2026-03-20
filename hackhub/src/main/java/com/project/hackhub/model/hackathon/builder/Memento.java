package com.project.hackhub.model.hackathon.builder;

public interface Memento {

    Memento restoreMemento();

    Memento saveMemento();

}
