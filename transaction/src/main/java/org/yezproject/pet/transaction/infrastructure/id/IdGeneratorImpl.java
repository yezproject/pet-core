package org.yezproject.pet.transaction.infrastructure.id;

import org.springframework.stereotype.Component;
import org.yezproject.pet.transaction.application.id.IdGenerator;

import java.util.UUID;

@Component
record IdGeneratorImpl() implements IdGenerator {

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
