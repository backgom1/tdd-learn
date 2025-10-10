package learn.tdd.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(columnList = "sellerId"))
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
    private LocalDateTime registeredTimeUtc;

    private Product(UUID id, UUID sellerId, String name, String description, String imageUrl, BigDecimal price, Integer stockQuantity, LocalDateTime registeredTimeUtc) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.registeredTimeUtc = registeredTimeUtc;
    }


    public static Product of(UUID id, UUID sellerId, String name, String description, String imageUrl, BigDecimal price, Integer stockQuantity, LocalDateTime registeredTimeUtc) {
        return new Product(id, sellerId, name, description, imageUrl, price, stockQuantity, registeredTimeUtc);
    }
}
