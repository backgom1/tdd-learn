package learn.tdd.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shopper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true)
    private UUID id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(length = 1000)
    private String hashedPassword;

    private Shopper(UUID id, String email, String username, String hashedPassword) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public static Shopper of(UUID id, String email, String username, String hashedPassword) {
        return new Shopper(id, email, username, hashedPassword);
    }
}
