package org.yproject.pet;

import static org.yproject.pet.RandomUtils.randomFrom;

public final class RandomEnums {
    private RandomEnums() {}

    public static String randomCurrency() {
        return randomFrom(Currency.values()).toString();
    }
}
