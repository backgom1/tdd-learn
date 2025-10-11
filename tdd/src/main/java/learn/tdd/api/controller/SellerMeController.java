package learn.tdd.api.controller;

import learn.tdd.domain.Seller;
import learn.tdd.infra.repository.SellerRepository;
import learn.tdd.view.SellerMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerMeController(SellerRepository sellerRepository) {

    @GetMapping("/seller/me")
    SellerMeView me(Principal user) {
        UUID uuid = UUID.fromString(user.getName());
        Seller seller = sellerRepository.findById(uuid).orElseThrow();
        return new SellerMeView(seller.getId(), seller.getEmail(), seller.getUsername(), seller.getContactEmail());
    }

}


