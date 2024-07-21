package org.yezproject.pet.authentication.application.id;

import java.util.function.Supplier;

@FunctionalInterface
public interface IdGenerator extends Supplier<String> {
}
