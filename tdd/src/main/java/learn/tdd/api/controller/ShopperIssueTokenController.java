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
public class ShopperIssueTokenController {

    @PostMapping("/shopper/issueToken")
    public AccessTokenCarrier issueToken() {
        return new AccessTokenCarrier("token");
    }
}
