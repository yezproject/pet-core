package org.yezproject.pet.domain_common.models;

public abstract class AggregateRoot<I> extends Entity<I> {
    protected AggregateRoot(I id) {
        super(id);
    }
}
