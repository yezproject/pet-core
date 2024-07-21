package org.yezproject.pet.authentication.infrastructure.id;

import org.springframework.stereotype.Component;
import org.yezproject.pet.authentication.application.id.IdGenerator;

import java.util.UUID;

@Component
record IdGeneratorImpl() implements IdGenerator {

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
