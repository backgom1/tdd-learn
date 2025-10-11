package learn.tdd.api.controller;

import learn.tdd.command.ChangeContactEmailCommand;
import learn.tdd.domain.Seller;
import learn.tdd.infra.repository.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

import static learn.tdd.infra.util.UserPropertyValidator.validEmail;

@RestController
public record SellerChangeContactEmailController(SellerRepository sellerRepository) {

    @PostMapping("/seller/changeContactEmail")
    ResponseEntity<?> changeContactEmail(@RequestBody ChangeContactEmailCommand command, Principal user) {
        var uuid = UUID.fromString(user.getName());
        Seller seller = sellerRepository.findById(uuid).orElseThrow();
        seller.updateContactEmail(command.contactEmail());
        sellerRepository.save(seller);
        if(validEmail(command.contactEmail())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
