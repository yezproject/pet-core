package org.yproject.pet.common.models;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class Entity<I> {
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
}
