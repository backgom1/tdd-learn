package learn.tdd.api.controller;

import io.jsonwebtoken.Jwts;
import learn.tdd.domain.Seller;
import learn.tdd.domain.Shopper;
import learn.tdd.infra.repository.SellerRepository;
import learn.tdd.infra.repository.ShopperRepository;
import learn.tdd.infra.util.JwtKeyHolder;
import learn.tdd.query.IssueSellerToken;
import learn.tdd.query.IssueShopperToken;
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
public class ShopperIssueTokenController {

    private final ShopperRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtKeyHolder jwtKeyHolder;

    public ShopperIssueTokenController(ShopperRepository repository, PasswordEncoder passwordEncoder, JwtKeyHolder jwtKeyHolder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtKeyHolder = jwtKeyHolder;
    }

    @PostMapping("/shopper/issueToken")
    ResponseEntity<?> issueToken(@RequestBody IssueShopperToken query) {
        return repository
                .findByEmail(query.email())
                .filter(shopper -> passwordEncoder.matches(
                        query.password(),
                        shopper.getHashedPassword()
                ))
                .map(this::composeToken)
                .map(AccessTokenCarrier::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken(Shopper shopper) {
        return Jwts
                .builder()
                .setSubject(shopper.getId().toString())
                .claim("scp", "shopper")
                .signWith(jwtKeyHolder.key())
                .compact();
    }
}
