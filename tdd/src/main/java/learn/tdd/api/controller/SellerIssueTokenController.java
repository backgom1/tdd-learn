package learn.tdd.api.controller;

import io.jsonwebtoken.Jwts;
import learn.tdd.domain.Seller;
import learn.tdd.infra.repository.SellerRepository;
import learn.tdd.infra.util.JwtKeyHolder;
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
    private final JwtKeyHolder jwtKeyHolder;

    @Value("${security.jwt.secret}")
    private String secret;

    @Autowired
    public SellerIssueTokenController(SellerRepository sellerRepository, PasswordEncoder passwordEncoder, JwtKeyHolder jwtKeyHolder) {
        this.sellerRepository = sellerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtKeyHolder = jwtKeyHolder;
    }


    @PostMapping("/seller/issueToken")
    ResponseEntity<?> issueToken(@RequestBody IssueSellerToken query) {
        return sellerRepository
                .findByEmail(query.email())
                .filter(seller -> passwordEncoder.matches(
                        query.password(),
                        seller.getHashPassword()
                ))
                .map(this::composeToken)
                .map(AccessTokenCarrier::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }


    private String composeToken(Seller seller) {
        return Jwts.builder()
                .setSubject(seller.getId().toString())
                .claim("scp", "seller")
                .signWith(jwtKeyHolder.key())
                .compact();
    }
}
