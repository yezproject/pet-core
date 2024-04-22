package org.yproject.pet.user.driving;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SavedUserDto {
    String id;
    String email;
    String password;
    String fullName;
    String role;
    String status;
    Instant createAt;
    Instant approvedAt;
}
