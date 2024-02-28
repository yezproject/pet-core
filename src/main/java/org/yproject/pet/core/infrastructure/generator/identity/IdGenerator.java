package org.yproject.pet.core.infrastructure.generator.identity;

import java.util.function.Supplier;

@FunctionalInterface
public interface IdGenerator extends Supplier<String> {
    String get();
}
