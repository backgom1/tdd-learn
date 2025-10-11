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

    @Column
    private String contactEmail;

    private Seller(UUID id, String email, String username, String hashPassword, String contactEmail) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.hashPassword = hashPassword;
        this.contactEmail = contactEmail;
    }

    public static Seller of(UUID id, String email, String username, String hashPassword, String contactEmail, PasswordEncoder encoder) {
        return new Seller(id, email, username, encoder.encode(hashPassword), contactEmail);
    }

    public void updateContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
