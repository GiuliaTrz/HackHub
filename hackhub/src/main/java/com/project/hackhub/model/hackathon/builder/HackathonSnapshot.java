package com.project.hackhub.model.hackathon.builder;

import com.project.hackhub.model.hackathon.Prenotazione;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.utente.UtenteRegistrato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HackathonSnapshot {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(unique = true)
    private UtenteRegistrato author;

    private String name;

    @Column(length = 5000)
    private String ruleBook;

    private LocalDate expiredSubscriptionsDate;

    private Integer maxTeamDimension;

    @Embedded
    private Soldi moneyPrice;

    @OneToOne(cascade = CascadeType.ALL)
    private Prenotazione reservation;

    @ElementCollection
    private List<UUID> mentorsList = new ArrayList<>();

    private UUID judge;
}