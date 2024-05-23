package org.yezproject.pet.id;

import org.yezproject.pet.Dao;

import java.util.function.Supplier;

@FunctionalInterface
@Dao
public interface IdGenerator extends Supplier<String> {
}
