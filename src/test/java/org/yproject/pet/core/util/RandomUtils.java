package org.yproject.pet.core.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomUtils {
    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    public static <T> T random(T[] t) {
        int randIndex = random().nextInt(t.length);
        return t[randIndex];
    }

    public static String randomShortString() {
        return RandomStringUtils.random(10);
    }

    public static String randomLongString() {
        return RandomStringUtils.random(30);
    }

    public static Instant randomInstant() {
        return randomInstantBetween(Instant.EPOCH, Instant.EPOCH.plus(365*100, ChronoUnit.DAYS));
    }

    public static Instant randomInstantBetween(Instant startInclusive, Instant endExclusive) {
        long startSeconds = startInclusive.getEpochSecond();
        long endSeconds = endExclusive.getEpochSecond();
        long random = random().nextLong(startSeconds, endSeconds);

        return Instant.ofEpochSecond(random);
    }
}
