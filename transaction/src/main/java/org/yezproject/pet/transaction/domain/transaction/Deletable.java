package org.yezproject.pet.transaction.domain.transaction;

public interface Deletable {
    boolean isDelete();

    DeleteInfo getDeleteInfo();

    void delete(String reason);
}
