package learn.tdd.api.controller;

import learn.tdd.command.CreateSellerCommand;
import learn.tdd.command.CreateShopperCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static learn.tdd.infra.util.UserPropertyValidator.*;

@RestController
public record ShopperSignUpController() {

    @PostMapping("/shopper/signup")
    ResponseEntity<?> signUp(@RequestBody CreateShopperCommand request) {

        if (validEmail(request.email())) {
            return ResponseEntity.badRequest().build();
        } else if (validUsername(request.username())) {
            return ResponseEntity.badRequest().build();
        } else if (validPassword(request.password())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }
}
