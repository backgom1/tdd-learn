package learn.tdd.api.controller;

import learn.tdd.command.CreateShopperCommand;
import learn.tdd.domain.Shopper;
import learn.tdd.infra.repository.ShopperRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static learn.tdd.infra.util.UserPropertyValidator.*;

@RestController
public record ShopperSignUpController(PasswordEncoder passwordEncoder,
                                      ShopperRepository shopperRepository) {

    @PostMapping("/shopper/signup")
    ResponseEntity<?> signUp(@RequestBody CreateShopperCommand request) {

        if (validEmail(request.email())) {
            return ResponseEntity.badRequest().build();
        } else if (validUsername(request.username())) {
            return ResponseEntity.badRequest().build();
        } else if (validPassword(request.password())) {
            return ResponseEntity.badRequest().build();
        }

        Shopper shopper = Shopper.of(UUID.randomUUID(), request.email(), request.username(), passwordEncoder.encode(request.password()));
        shopperRepository.save(shopper);

        return ResponseEntity.noContent().build();
    }
}
