package learn.tdd.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long datakey;


    private UUID id;

    private String email;

    private String username;

    @Column(length = 1000)
    private String hashPassword;


    private Seller(UUID id, String email, String username, String hashPassword) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.hashPassword = hashPassword;
    }

    public static Seller of(UUID id, String email, String username, String hashPassword, PasswordEncoder encoder) {
        return new Seller(id, email, username, encoder.encode(hashPassword));
    }
}
