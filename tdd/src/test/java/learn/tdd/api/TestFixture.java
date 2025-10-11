package learn.tdd.api;

import learn.tdd.command.CreateSellerCommand;
import learn.tdd.command.CreateShopperCommand;
import learn.tdd.command.RegisterProductCommand;
import learn.tdd.infra.repository.ProductRepository;
import learn.tdd.query.IssueSellerToken;
import learn.tdd.result.AccessTokenCarrier;
import learn.tdd.result.PageCarrier;
import learn.tdd.view.ProductView;
import learn.tdd.view.SellerMeView;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.*;
import static learn.tdd.api.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static learn.tdd.infra.util.GeneratorUtil.*;

public record TestFixture(TestRestTemplate client, ProductRepository productRepository) {

    public static TestFixture create(Environment environment, ProductRepository productRepository) {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.setUriTemplateHandler(new LocalHostUriTemplateHandler(environment));
        return new TestFixture(testRestTemplate, productRepository);
    }

    public String createShopperThenIssueToken() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        return issueShopperToken(email, password);
    }

    public void createShopper(String email, String username, String password) {
        var command = new CreateShopperCommand(
                email,
                username,
                password
        );
        ensureSuccessful(
                client.postForEntity("/shopper/signup", command, Void.class), command);
    }

    private void ensureSuccessful(ResponseEntity<Void> response, Object request) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            String message = "Request with " + request + " failed with status code " + response.getStatusCode();
            throw new RuntimeException(message);
        }
    }

    public String issueShopperToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
                "/shopper/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );
        return carrier.accessToken();
    }

    public void setShopperAsDefaultUser(String email, String password) {
        String token = issueShopperToken(email, password);
        setDefaultAuthorization("Bearer " + token);
    }

    private void setDefaultAuthorization(String authorization) {
        RestTemplate restTemplate = client.getRestTemplate();
        restTemplate.getInterceptors().addFirst((request, body, execution) -> {
            if (!request.getHeaders().containsKey("Authorization")) {
                request.getHeaders().add("Authorization", authorization);
            }
            return execution.execute(request, body);
        });
    }

    public void createSellerThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        String contactEmail = generateEmail();
        createSeller(email, generateUsername(), password, contactEmail);
        setSellerAsDefaultUser(email, password);
    }

    public void createSeller(String email, String username, String password, String contactEmail) {
        CreateSellerCommand command = new CreateSellerCommand(email, username, password, contactEmail);
        client.postForEntity("/seller/signup", command, Void.class);
    }

    private void setSellerAsDefaultUser(String email, String password) {
        String token = issueSellerToken(email, password);
        setDefaultAuthorization("Bearer " + token);

    }


    private String issueSellerToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );
        return carrier.accessToken();
    }

    public void createShopperThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        setShopperAsDefaultUser(email, password);
    }

    public UUID registerProduct() {
        return registerProduct(generateRegisterProductCommand());
    }

    public UUID registerProduct(RegisterProductCommand command) {
        ResponseEntity<Void> response = client.postForEntity("/seller/products", command, Void.class);
        URI location = response.getHeaders().getLocation();
        String path = requireNonNull(location).getPath();
        String id = path.substring("/seller/products/".length());
        return UUID.fromString(id);
    }

    public List<UUID> registerProducts() {
        return List.of(registerProduct(), registerProduct(), registerProduct());
    }

    public List<UUID> registerProducts(int size) {
        List<UUID> ids = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ids.add(registerProduct());
        }
        return ids;
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public SellerMeView getSeller() {
        return client.getForObject("/seller/me", SellerMeView.class);
    }

    public String consumeProductPage() {
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
                RequestEntity.get("/shopper/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );
        return requireNonNull(response.getBody()).continuationToken();
    }

    //페이지 토큰을 두개 사용하는 메서드
    public String consumeTwoProductPages() {
        String token = consumeProductPage();
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
                RequestEntity.get("/shopper/products?continuationToken=" + token).build(),
                new ParameterizedTypeReference<>() {
                }
        );
        return requireNonNull(response.getBody()).continuationToken();
    }
}
