package learn.tdd.api.controller;

import learn.tdd.command.CreateSellerCommand;
import learn.tdd.domain.Seller;
import learn.tdd.infra.repository.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerSignUpController(SellerRepository sellerRepository, PasswordEncoder encoder) {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]*$";


    @PostMapping("/seller/signup")
    ResponseEntity<?> signUp(@RequestBody CreateSellerCommand request) {
        if (validEmail(request)) {
            return ResponseEntity.badRequest().build();
        } else if (validUsername(request)) {
            return ResponseEntity.badRequest().build();
        } else if (validPassword(request)) {
            return ResponseEntity.badRequest().build();
        }

        if (sellerRepository.existsByEmailOrUsername(request.email(), request.username())) {
            return ResponseEntity.badRequest().build();
        }
        sellerRepository.save(Seller.of(request.email(), request.username(), request.password(), encoder));


        return ResponseEntity.noContent().build();
    }

    private boolean validPassword(CreateSellerCommand request) {
        return request.password() == null || request.password().isBlank() || request.password().length() < 8;
    }

    private boolean validUsername(CreateSellerCommand request) {
        return request.username() == null || request.username().isBlank() || request.username().length() < 3 || !request.username().matches(USERNAME_REGEX);
    }

    private boolean validEmail(CreateSellerCommand request) {
        return request.email() == null || !request.email().contains("@") || request.email().endsWith("@") || !request.email().matches(EMAIL_REGEX);
    }

}
