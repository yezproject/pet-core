package org.yezproject.pet.transaction.application.id;

import java.util.function.Supplier;

@FunctionalInterface
public interface IdGenerator extends Supplier<String> {
}
