package learn.tdd.querymodel;

import learn.tdd.domain.Product;
import learn.tdd.domain.Seller;
import learn.tdd.view.ProductView;
import learn.tdd.view.SellerView;

record ProductSellerTuple(Product product, Seller seller) {
    ProductView toView() {
        return new ProductView(
                product().getId(),
                new SellerView(seller().getId(), seller().getUsername(),null),
                product().getName(),
                product().getImageUrl(),
                product().getDescription(),
                product().getPrice(),
                product().getStockQuantity()
        );
    }
}
