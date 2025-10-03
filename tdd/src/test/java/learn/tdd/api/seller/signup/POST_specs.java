package learn.tdd.api.seller.signup;

import learn.tdd.TddApplication;
import learn.tdd.command.CreateSellerCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(
        classes = TddApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /seller/signup")
public class POST_specs {

    @Test
    @DisplayName("올바르게 요청하면 204 No Content 상태코드를 반환한다.")
    void response_should_return_204_when_request_is_correct(@Autowired TestRestTemplate client) {
        // AAA 패턴

        //Arrange
        var command = new CreateSellerCommand("seller@test.com", "seller", "password");

        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);

        //Assert
        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(204);
    }
}
