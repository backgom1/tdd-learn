package learn.tdd.api.seller.issuetoken;

import learn.TddApiTest;
import learn.tdd.command.CreateSellerCommand;
import learn.tdd.query.IssueSellerToken;
import learn.tdd.result.AccessTokenCarrier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static java.util.Objects.requireNonNull;
import static learn.tdd.JwtAssertions.conformsToJwtFormat;
import static learn.tdd.infra.util.GeneratorUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@TddApiTest
@DisplayName("POST /seller/issueToken")
public class POST_specs {


    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
                "/seller/signup",
                new CreateSellerCommand(
                        email,
                        generateUsername(),
                        password
                ),
                Void.class
        );

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 올바르게_요청하면_접근_토큰을_반환한다(
            @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
                "/seller/signup",
                new CreateSellerCommand(
                        email,
                        generateUsername(),
                        password
                ),
                Void.class
        );

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getBody().accessToken()).isNotNull();
    }


    @Test
    void 접근_토큰은_JWT_형식을_따른다(
            @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
                "/seller/signup",
                new CreateSellerCommand(
                        email,
                        generateUsername(),
                        password
                ),
                Void.class
        );

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );

        // Assert
        String actual = requireNonNull(response.getBody()).accessToken();
        assertThat(actual).satisfies(conformsToJwtFormat());
    }
    @Test
    void 존재하지_않는_이메일_주소가_사용되면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 잘못된_비밀번호가_사용되면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        String wrongPassword = generatePassword();

        client.postForEntity(
                "/seller/signup",
                new CreateSellerCommand(
                        email,
                        generateUsername(),
                        password
                ),
                Void.class
        );

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/seller/issueToken",
                new IssueSellerToken(email, wrongPassword),
                AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
