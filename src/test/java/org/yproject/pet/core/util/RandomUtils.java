package org.yproject.pet.core.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public final class RandomUtils {
    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    public static <T> T randomFrom(T[] t) {
        int randIndex = random().nextInt(t.length);
        return t[randIndex];
    }

    public static <T> List<T> randomShortList(Supplier<T> randomSupplier) {
        int shortSize = random().nextInt(5) + 5;
        List<T> list = new ArrayList<>(shortSize);
        for (int i = 0; i < shortSize; i++) {
            list.add(i, randomSupplier.get());
        }
        return list;
    }

    public static String randomShortString() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String randomLongString() {
        return RandomStringUtils.randomAlphanumeric(30);
    }

    public static Instant randomInstant() {
        return randomInstantBetween(Instant.EPOCH, Instant.EPOCH.plus(365 * 100, ChronoUnit.DAYS));
    }

    public static Instant randomInstantBetween(Instant startInclusive, Instant endExclusive) {
        long startSeconds = startInclusive.getEpochSecond();
        long endSeconds = endExclusive.getEpochSecond();
        long random = random().nextLong(startSeconds, endSeconds);

        return Instant.ofEpochSecond(random);
    }

    public static double randomDouble() {
        return random().nextDouble();
    }

}
