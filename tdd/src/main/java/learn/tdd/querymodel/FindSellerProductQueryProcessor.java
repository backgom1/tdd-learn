package learn.tdd.querymodel;

import learn.tdd.domain.Product;
import learn.tdd.query.FindSellerProduct;
import learn.tdd.view.SellerProductView;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

//프로덕트 엔티티를 조회하는 함수 인터페이스에 의존
public class FindSellerProductQueryProcessor {

    private final Function<UUID, Optional<Product>> findProduct;

    public FindSellerProductQueryProcessor(Function<UUID, Optional<Product>> findProduct) {
        this.findProduct = findProduct;
    }

    public static SellerProductView convertToView(Product product) {
        return new SellerProductView(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getRegisteredTimeUtc()
        );
    }

    public Optional<SellerProductView> process(FindSellerProduct query) {
        return findProduct
                .apply(query.productId())
                .filter(product -> product.getSellerId().equals(query.sellerId()))
                .map(FindSellerProductQueryProcessor::convertToView);
    }
}
