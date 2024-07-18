package org.yezproject.pet.transaction.application.user.driving;

public interface PasswordService {

    boolean matches(String raw, String encodedPassword);

    String encode(String raw);
}
