package learn.tdd.view;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductView(UUID id, SellerView seller, String name, String imageUrl, String description,
                          BigDecimal priceAmount, int stockQuantity) {
}
