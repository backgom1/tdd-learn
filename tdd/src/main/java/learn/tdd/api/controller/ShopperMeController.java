package learn.tdd.api.controller;

import learn.tdd.domain.Shopper;
import learn.tdd.infra.repository.ShopperRepository;
import learn.tdd.view.ShopperMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeController(ShopperRepository shopperRepository) {


    @GetMapping("/shopper/me")
    ShopperMeView me(Principal user){
        UUID id = UUID.fromString(user.getName());
        Shopper shopper = shopperRepository.findById(id).orElseThrow();
        return new ShopperMeView(shopper.getId(), shopper.getEmail(), shopper.getUsername());
    }
}
