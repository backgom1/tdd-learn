package learn.tdd.api.controller;

import learn.tdd.command.CreateSellerCommand;
import learn.tdd.domain.Seller;
import learn.tdd.infra.repository.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static learn.tdd.infra.util.UserPropertyValidator.*;

@RestController
public record SellerSignUpController(SellerRepository sellerRepository, PasswordEncoder encoder) {

    @PostMapping("/seller/signup")
    ResponseEntity<?> signUp(@RequestBody CreateSellerCommand request) {
        if (validEmail(request.email())) {
            return ResponseEntity.badRequest().build();
        } else if (validUsername(request.username())) {
            return ResponseEntity.badRequest().build();
        } else if (validPassword(request.password())) {
            return ResponseEntity.badRequest().build();
        }

        if (sellerRepository.existsByEmailOrUsername(request.email(), request.username())) {
            return ResponseEntity.badRequest().build();
        }
        sellerRepository.save(Seller.of(UUID.randomUUID(), request.email(), request.username(), request.password(), encoder));


        return ResponseEntity.noContent().build();
    }

}
