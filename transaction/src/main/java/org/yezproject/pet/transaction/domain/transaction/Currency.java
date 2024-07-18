package org.yezproject.pet.transaction.domain.transaction;

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
