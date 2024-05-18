package org.yproject.pet.transaction;

import lombok.Getter;

@Getter
public enum Currency {
    VND("₫"),
    USD("$");
    private final String symbol;

    Currency (final String symbol) {
        this.symbol = symbol;
    }
}
