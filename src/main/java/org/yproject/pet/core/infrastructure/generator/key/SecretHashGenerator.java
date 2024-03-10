package org.yproject.pet.core.infrastructure.generator.key;

@FunctionalInterface
public interface SecretHashGenerator {
    String get(String original);
}
