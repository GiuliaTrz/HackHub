package com.project.hackhub.dto;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Report;

import java.util.List;

public record Informazioni() {

    private static List<Hackathon> listaHackathonAttivi;

    private static Report report;
}
