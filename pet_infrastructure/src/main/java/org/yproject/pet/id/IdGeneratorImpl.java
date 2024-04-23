package org.yproject.pet.id;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
record IdGeneratorImpl() implements IdGenerator {

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
