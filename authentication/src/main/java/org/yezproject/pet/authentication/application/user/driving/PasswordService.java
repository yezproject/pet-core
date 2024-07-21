package org.yezproject.pet.authentication.application.user.driving;

public interface PasswordService {

    boolean matches(String raw, String encodedPassword);

    String encode(String raw);
}
