package org.yproject.pet.common.models;

public abstract class AggregateRoot<I> extends Entity<I> {
    protected AggregateRoot(I id) {
        super(id);
    }
}
