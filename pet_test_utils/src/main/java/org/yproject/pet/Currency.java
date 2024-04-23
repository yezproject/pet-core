package org.yproject.pet;

/* Cloned from Domain Layer */
enum Currency {
    VND("₫"),
    USD("$");
    private final String symbol;

    Currency(final String symbol) {
        this.symbol = symbol;
    }
}
