package org.yezproject.pet.web.apis;

import org.yezproject.pet.jwt.JwtPayload;

import static org.yezproject.pet.RandomUtils.randomInstant;
import static org.yezproject.pet.RandomUtils.randomShortString;

public record TestUtils() {

    public static JwtPayload randomJwtPayload() {
        return new JwtPayload(randomShortString(), randomInstant(), randomShortString());
    }
}
