package learn.tdd.api.controller;

import learn.tdd.command.RegisterProductCommand;
import learn.tdd.domain.Seller;
import learn.tdd.infra.repository.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URL;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
public record SellerProductsController(SellerRepository sellerRepository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProducts(Principal user, @RequestBody RegisterProductCommand command) {
        UUID uuid = UUID.fromString(user.getName());
        Optional<Seller> seller = sellerRepository.findById(uuid);
        if (seller.isEmpty()) {
            return ResponseEntity.status(403).build();
        } else if (!isValidImageUrl(command.imageUrl())) {
            return ResponseEntity.status(400).build();
        }

        URI uri = URI.create("/seller/products/" + UUID.randomUUID());
        return ResponseEntity.created(uri).build();
    }

    private boolean isValidImageUrl(String imageUrl) {
        try {
            URI uri = URI.create(imageUrl);
            return uri.getHost() != null;
        } catch (Exception exception) {
            return false;
        }
    }
}
