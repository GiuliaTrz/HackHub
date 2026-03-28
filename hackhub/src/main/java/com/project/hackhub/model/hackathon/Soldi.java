package com.project.hackhub.model.hackathon;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Currency;

@Embeddable
public final class Soldi {

    @Getter
    private double quantity;
    private Currency currency;

}
