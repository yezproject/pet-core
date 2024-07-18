package org.yezproject.pet.transaction.infrastructure.web.apis;

import org.yezproject.pet.transaction.application.jwt.JwtPayload;

import static org.yezproject.pet.test_common.RandomUtils.randomInstant;
import static org.yezproject.pet.test_common.RandomUtils.randomShortString;

public record TestUtils() {

    public static JwtPayload randomJwtPayload() {
        return new JwtPayload(randomShortString(), randomInstant(), randomShortString());
    }
}
