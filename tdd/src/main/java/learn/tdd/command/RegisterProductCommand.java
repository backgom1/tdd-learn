package learn.tdd.command;

import java.math.BigDecimal;

public record RegisterProductCommand(String name, String imageUrl, String description, BigDecimal priceAmount,int stockQuantity) {
}
