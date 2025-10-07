package learn.tdd.api;

import learn.tdd.command.CreateSellerCommand;
import learn.tdd.command.CreateShopperCommand;
import learn.tdd.query.IssueSellerToken;
import learn.tdd.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import static learn.tdd.infra.util.GeneratorUtil.*;

public record TestFixture(TestRestTemplate client) {

    public static TestFixture create(Environment environment) {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.setUriTemplateHandler(new LocalHostUriTemplateHandler(environment));
        return new TestFixture(testRestTemplate);
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
        client.postForEntity("/shopper/signup", command, Void.class);
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
        createSeller(email, generateUsername(), password);
        setSellerAsDefaultUser(email, password);
    }

    private void createSeller(String email, String username, String password) {
        CreateSellerCommand command = new CreateSellerCommand(email, username, password);
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
}
