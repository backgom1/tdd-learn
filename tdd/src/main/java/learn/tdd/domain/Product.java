package learn.tdd.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;
    private UUID id;
    private UUID sellerId;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private Integer stockQuantity;

    private Product(UUID id, UUID sellerId, String name, String description, String imageUrl, BigDecimal price, Integer stockQuantity) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }


    public static Product of(UUID id, UUID sellerId, String name, String description, String imageUrl, BigDecimal price, Integer stockQuantity) {
        return new Product(id, sellerId, name, description, imageUrl, price, stockQuantity);
    }
}
