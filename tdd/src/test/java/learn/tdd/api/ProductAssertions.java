package learn.tdd.api;

import learn.tdd.command.RegisterProductCommand;
import learn.tdd.view.SellerProductView;
import org.assertj.core.api.ThrowingConsumer;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAssertions {

    public static ThrowingConsumer<? super SellerProductView> isDerivedFrom(
            RegisterProductCommand command
    ) {
        return product -> {
            assertThat(product.name()).isEqualTo(command.name());
            assertThat(product.description()).isEqualTo(command.description());
            assertThat(product.priceAmount()).matches(equals(command.priceAmount()));
            assertThat(product.stockQuantity()).isEqualTo(command.stockQuantity());
            assertThat(product.imageUrl()).isEqualTo(command.imageUrl());
        };
    }

    private static Predicate<? super BigDecimal> equals(BigDecimal expected) {
        return actual -> actual.compareTo(expected) == 0;
    }
}
