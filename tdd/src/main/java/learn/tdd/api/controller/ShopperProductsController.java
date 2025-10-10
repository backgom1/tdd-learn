package learn.tdd.api.controller;

import jakarta.persistence.EntityManager;
import learn.tdd.query.GetProductPage;
import learn.tdd.querymodel.GetProductPageQueryProcessor;
import learn.tdd.result.PageCarrier;
import learn.tdd.view.ProductView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperProductsController(EntityManager em) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts(@RequestParam(required = false) String continuationToken) {
        var processor = new GetProductPageQueryProcessor(em);
        var query = new GetProductPage(continuationToken);
        return processor.process(query);
    }

}
