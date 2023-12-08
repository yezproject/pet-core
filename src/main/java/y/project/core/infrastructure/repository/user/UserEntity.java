package y.project.core.infrastructure.repository.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class UserEntity{
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant createAt;

    @Column()
    private Instant approvedAt;
}
