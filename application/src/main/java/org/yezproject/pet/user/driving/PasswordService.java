package org.yezproject.pet.user.driving;

public interface PasswordService {

    boolean matches(String raw, String encodedPassword);

    String encode(String raw);
}
