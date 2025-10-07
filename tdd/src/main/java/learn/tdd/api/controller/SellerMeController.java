package learn.tdd.api.controller;

import learn.view.SellerMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerMeController() {

    @GetMapping("/seller/me")
    SellerMeView me(Principal user) {
        String uuid = UUID.fromString(user.getName()).toString();
        return new SellerMeView(uuid, null, null, null);
    }

}


