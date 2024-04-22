package org.yproject.pet.id;

import org.yproject.pet.Dao;

import java.util.function.Supplier;

@FunctionalInterface
@Dao
public interface IdGenerator extends Supplier<String> {
}
