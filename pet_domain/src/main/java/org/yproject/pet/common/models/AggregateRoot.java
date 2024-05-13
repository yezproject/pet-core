package org.yproject.pet.common.models;

public abstract class AggregateRoot<I extends EntityId> extends Entity<I> {
    protected AggregateRoot(I id) {
        super(id);
    }
}
