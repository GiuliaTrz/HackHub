package com.project.hackhub.model.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.project.hackhub.model.hackathon.Hackathon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Submission {
    @Id @GeneratedValue
    private UUID ids;
    @ManyToOne
    @JsonIgnoreProperties({"hackathon", "teamMembersList", "invitationList", "hasPendingCallProposal"
    , "teamLeader"})
    private Team team;
    @Embedded
    private FileTemplate fileTemplate;

    private LocalDateTime timestamp;

    private Float grade;
    private String writtenEvaluation;

    @ManyToOne
    @JoinColumn(name = "hackathon_id", nullable = false) // <--- Cambiato da "id" a "hackathon_id"
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIncludeProperties({"id", "name"})
    private Hackathon hackathon;

    public Submission(Team team, FileTemplate fileTemplate){
        if (team == null || fileTemplate == null )
            throw new IllegalArgumentException("Invalid submission: can't have null arguments");
        this.team = team;
        this.fileTemplate = fileTemplate;
        this.timestamp = LocalDateTime.now();
    }
}
