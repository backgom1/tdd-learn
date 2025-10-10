package learn.tdd.querymodel;

import learn.tdd.domain.Product;
import learn.tdd.query.GetSellerProducts;
import learn.tdd.result.ArrayCarrier;
import learn.tdd.view.SellerProductView;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

public class GetSellerProductsQueryProcessor {

    private final Function<UUID, List<Product>> getProductsOfSeller;

    public GetSellerProductsQueryProcessor(Function<UUID, List<Product>> getProductsOfSeller) {
        this.getProductsOfSeller = getProductsOfSeller;
    }


    public ArrayCarrier<SellerProductView> processor(GetSellerProducts query) {
        SellerProductView[] productViews = getProductsOfSeller.apply(query.sellerId())
                .stream()
                .sorted(comparing(Product::getRegisteredTimeUtc, reverseOrder()))
                .map(FindSellerProductQueryProcessor::convertToView)
                .toArray(SellerProductView[]::new);
        return new ArrayCarrier<>(productViews);
    }
}
