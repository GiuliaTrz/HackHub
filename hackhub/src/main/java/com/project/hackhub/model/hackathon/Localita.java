package com.project.hackhub.model.hackathon;

import jakarta.persistence.Embeddable;

@Embeddable
public final class Localita {

    private String name;
    private String province;
    private int cap;
    private String address;
}
