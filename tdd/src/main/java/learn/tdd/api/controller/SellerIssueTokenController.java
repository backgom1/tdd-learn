package learn.tdd.api.controller;

import io.jsonwebtoken.Jwts;
import learn.tdd.domain.Seller;
import learn.tdd.infra.repository.SellerRepository;
import learn.tdd.query.IssueSellerToken;
import learn.tdd.result.AccessTokenCarrier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

@RestController
public class SellerIssueTokenController {


    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.jwt.secret}")
    private String secret;

    @Autowired
    public SellerIssueTokenController(SellerRepository sellerRepository, PasswordEncoder passwordEncoder) {
        this.sellerRepository = sellerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/seller/issueToken")
    public ResponseEntity<?> issueToken(@RequestBody IssueSellerToken request) {
        Optional<Seller> optionalSeller = sellerRepository.findByEmail(request.email());
        if (optionalSeller.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Seller seller = optionalSeller.get();

        if (!passwordEncoder.matches(request.password(), seller.getHashPassword())) {
            return ResponseEntity.badRequest().build();
        }


        return ResponseEntity.ok(new AccessTokenCarrier(Jwts
                .builder()
                .signWith(new SecretKeySpec(secret.getBytes(), "HmacSHA256"))
                .compact()));
    }
}
