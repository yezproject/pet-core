package org.yezproject.pet.test_common;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public final class RandomUtils {
    private RandomUtils() {
    }

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

    public static <T> Set<T> randomShortSet(Supplier<T> randomSupplier) {
        int shortSize = random().nextInt(5) + 5;
        Set<T> set = new HashSet<>();
        for (int i = 0; i < shortSize; i++) {
            set.add(randomSupplier.get());
        }
        return set;
    }

    public static String randomShortString() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String randomNullableShortString() {
        return randomFrom(new String[]{null, RandomStringUtils.randomAlphanumeric(10)});
    }

    public static String randomLongString() {
        return RandomStringUtils.randomAlphanumeric(30);
    }

    public static Instant randomInstant() {
        return randomInstantBetween(Instant.EPOCH, Instant.EPOCH.plus(365L * 100, ChronoUnit.DAYS));
    }

    public static Instant randomInstantBetween(Instant startInclusive, Instant endExclusive) {
        long startSeconds = startInclusive.getEpochSecond();
        long endSeconds = endExclusive.getEpochSecond();
        long random = random().nextLong(startSeconds, endSeconds);

        return Instant.ofEpochSecond(random);
    }

    public static double randomPositiveDouble() {
        return random().nextDouble();
    }

    public static double randomNegativeDouble() {
        return -random().nextDouble();
    }

}
