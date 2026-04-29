package com.project.hackhub.model.hackathon;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Location {

    private String name;
    private String province;
    private int cap;
    private String address;
}
