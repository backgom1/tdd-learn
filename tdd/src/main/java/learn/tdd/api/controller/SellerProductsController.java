package learn.tdd.api.controller;

import learn.tdd.command.RegisterProductCommand;
import learn.tdd.commandmodel.RegisterProductCommandExecutor;
import learn.tdd.infra.repository.ProductRepository;
import learn.tdd.infra.repository.SellerRepository;
import learn.tdd.query.FindSellerProduct;
import learn.tdd.query.GetSellerProducts;
import learn.tdd.querymodel.FindSellerProductQueryProcessor;
import learn.tdd.querymodel.GetSellerProductsQueryProcessor;
import learn.tdd.result.ArrayCarrier;
import learn.tdd.view.SellerProductView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

@RestController
public record SellerProductsController(SellerRepository sellerRepository, ProductRepository productRepository) {


    @PostMapping("/seller/products")
    ResponseEntity<?> registerProducts(Principal user, @RequestBody RegisterProductCommand command) {
        UUID id = UUID.randomUUID();
        var executor = new RegisterProductCommandExecutor(productRepository::save); //2. CRUD를 동작할 실행기 클래스에 값을 담기
        executor.execute(id, UUID.fromString(user.getName()), command);
        URI uri = URI.create("/seller/products/" + id);
        return ResponseEntity.created(uri).build();
    }


    @GetMapping("/seller/products")
    ArrayCarrier<SellerProductView> findAllProducts(Principal user) {
        var processor = new GetSellerProductsQueryProcessor(productRepository::findAllBySellerId);
        var query = new GetSellerProducts(UUID.fromString(user.getName()));
        return processor.processor(query);
    }

    @GetMapping("/seller/products/{id}")
    ResponseEntity<?> findProduct(@PathVariable UUID id, Principal user) {
        var processor = new FindSellerProductQueryProcessor(productRepository::findById);
        var query = new FindSellerProduct(UUID.fromString(user.getName()), id);
        return ResponseEntity.of(processor.process(query)); //of메서드는 값이 있으면 200과 본문 없으면 404 not found 반환
    }

}
