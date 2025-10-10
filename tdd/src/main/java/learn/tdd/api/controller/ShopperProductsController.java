package learn.tdd.api.controller;

import jakarta.persistence.EntityManager;
import learn.tdd.result.PageCarrier;
import learn.tdd.view.ProductView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
public record ShopperProductsController(EntityManager em) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts(@RequestParam(required = false) String continuationToken) {
        String query = """
                SELECT new learn.tdd.api.controller.ProductSellerTuple(p,s) 
                FROM Product p 
                JOIN FETCH Seller s ON p.sellerId = s.id 
                WHERE :cursor IS NULL OR p.dataKey <= :cursor
                ORDER BY p.dataKey DESC
                """;
        int pageSize = 10;
        List<ProductSellerTuple> resultList = em.createQuery(query, ProductSellerTuple.class)
                .setMaxResults(pageSize + 1)
                .setParameter("cursor", decodeCursor(continuationToken))
                .getResultList();

        ProductView[] items = resultList//조회되지 않는 대상의 데이터가 들어가기 때문에 limit를 걸자
                .stream()
                .limit(pageSize)
                .map(ProductSellerTuple::toView)
                .toArray(ProductView[]::new);

        Long nextPage = resultList.size() <= pageSize ? null : resultList.getLast().product().getDataKey();
        return new PageCarrier<>(items, encodeCursor(nextPage));

    }

    private Long decodeCursor(String continuationToken) {
        if (continuationToken == null || continuationToken.isBlank()) {
            return null;
        }
        byte[] decode = Base64.getUrlDecoder().decode(continuationToken);
        return Long.parseLong(new String(decode, UTF_8));
    }

    private String encodeCursor(Long nextPage) {
        if (nextPage == null) {
            return null;
        }
        byte[] data = nextPage.toString().getBytes(UTF_8);
        return Base64.getEncoder().encodeToString(data);
    }

}
