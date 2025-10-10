package learn.tdd.commandmodel;

import learn.tdd.command.RegisterProductCommand;
import learn.tdd.domain.Product;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static java.time.ZoneOffset.UTC;

public record RegisterProductCommandExecutor(Consumer<Product> saveProduct) {

    //3. 메서드 추출 후 이동 및 동작
    public void execute(UUID productId, UUID sellerId, RegisterProductCommand command) {
        validateCommand(command);
        Product product = Product.of(productId, sellerId, command.name(), command.description(), command.imageUrl(), command.priceAmount(), command.stockQuantity(),
                LocalDateTime.now(UTC));
        saveProduct(product);//1. 함수형 인터페이스로 빼기
    }

    private void validateCommand(RegisterProductCommand command) {
        if (!isValidImageUrl(command.imageUrl())) {
            throw new InvalidCommandException();
        }
    }

    private void saveProduct(Product product) {
        saveProduct.accept(product);
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
