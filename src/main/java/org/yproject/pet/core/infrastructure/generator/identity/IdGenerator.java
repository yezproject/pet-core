package org.yproject.pet.core.infrastructure.generator.identity;

import java.util.function.Supplier;

public interface IdGenerator extends Supplier<String> {
    String get();
}
