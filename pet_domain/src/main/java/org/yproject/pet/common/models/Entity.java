package org.yproject.pet.common.models;

import java.util.Objects;

public abstract class Entity<I extends EntityId> {
    final I id;

    Entity(I id) {
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Entity<?> entity = (Entity<?>) obj;
        return this.id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id.getId();
    }
}
