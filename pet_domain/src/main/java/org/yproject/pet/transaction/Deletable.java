package org.yproject.pet.transaction;

public interface Deletable {
    boolean isDelete();

    DeleteInfo getDeleteInfo();

    void delete(String reason);
}
