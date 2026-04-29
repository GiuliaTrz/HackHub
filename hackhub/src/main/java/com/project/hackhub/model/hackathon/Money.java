package com.project.hackhub.model.hackathon;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public final class Money {

    private double quantity;
    private String currency;

    public Money(Double quantity, String currency) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-null and >= 0");
        }
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("Currency code must be non-null and non-empty");
        }
        this.quantity = quantity;
        this.currency = currency;
    }
}
