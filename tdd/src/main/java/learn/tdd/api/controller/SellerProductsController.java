package learn.tdd.api.controller;

import learn.tdd.command.RegisterProductCommand;
import learn.tdd.domain.Product;
import learn.tdd.infra.repository.ProductRepository;
import learn.tdd.infra.repository.SellerRepository;
import learn.tdd.view.SellerProductView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

@RestController
public record SellerProductsController(SellerRepository sellerRepository, ProductRepository productRepository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProducts(Principal user, @RequestBody RegisterProductCommand command) {
        if (!isValidImageUrl(command.imageUrl())) {
            return ResponseEntity.status(400).build();
        }
        UUID uuid = UUID.randomUUID();

        UUID sellerId = UUID.fromString(user.getName());

        Product product = Product.of(uuid, sellerId, command.name(), command.description(), command.imageUrl(), command.priceAmount(), command.stockQuantity());
        productRepository.save(product);
        URI uri = URI.create("/seller/products/" + uuid);
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

    @GetMapping("/seller/products/{id}")
    ResponseEntity<?> findProduct(@PathVariable UUID id, Principal user) {
        UUID uuid = UUID.fromString(user.getName());
        return productRepository.findById(id)
                .filter(product -> product.getSellerId().equals(uuid))
                .map(product -> new SellerProductView(
                        product.getId(),
                        product.getName(),
                        product.getImageUrl(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        LocalDateTime.now(UTC)
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
