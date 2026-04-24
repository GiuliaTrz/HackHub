package com.project.hackhub.model.team;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Embeddable
public class FileTemplate {
    private String fileName;
}
