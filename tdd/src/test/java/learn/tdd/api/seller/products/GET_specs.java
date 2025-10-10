package learn.tdd.api.seller.products;

import learn.TddApiTest;
import learn.tdd.api.TestFixture;
import learn.tdd.command.RegisterProductCommand;
import learn.tdd.result.ArrayCarrier;
import learn.tdd.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.SECONDS;
import static learn.tdd.api.ProductAssertions.isDerivedFrom;
import static learn.tdd.api.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.reverseOrder;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.get;

@TddApiTest
@DisplayName("GET /seller/products")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
            @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();

        // Act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<>() {
                        }
                );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 판매자가_등록한_모든_상품을_반환한다(
            @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        List<UUID> ids = fixture.registerProducts();

        // Act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<>() {
                        }
                );

        // Assert
        ArrayCarrier<SellerProductView> actual = response.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual.items())
                .extracting(SellerProductView::id)
                .containsAll(ids);
    }

    @Test
    void 다른_판매자가_등록한_상품이_포함되지_않는다(
            @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        UUID unexpected = fixture.registerProduct();

        fixture.createSellerThenSetAsDefaultUser();
        fixture.registerProducts();

        // Act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<>() {
                        }
                );

        // Assert
        assertThat(requireNonNull(response.getBody()).items())
                .extracting(SellerProductView::id)
                .doesNotContain(unexpected);
    }

    @Test
    void 상품_속성을_올바르게_설정한다(
            @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        RegisterProductCommand command = generateRegisterProductCommand();
        fixture.registerProduct(command);

        // Act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<>() {
                        }
                );

        // Assert
        ArrayCarrier<SellerProductView> body = response.getBody();
        SellerProductView actual = requireNonNull(body).items()[0];
        assertThat(actual).satisfies(isDerivedFrom(command));
    }

    @Test
    void 상품_등록_시각을_올바르게_반환한다(
            @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        LocalDateTime referenceTime = LocalDateTime.now(UTC);
        fixture.registerProduct();

        // Act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<>() {
                        }
                );

        // Assert
        ArrayCarrier<SellerProductView> body = response.getBody();
        SellerProductView actual = requireNonNull(body).items()[0];
        assertThat(actual.registeredTimeUtc())
                .isCloseTo(referenceTime, within(1, SECONDS));
    }

    @Test
    void 상품_목록을_등록_시점_역순으로_정렬한다(
            @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        fixture.registerProducts();

        // Act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<>() {
                        }
                );

        // Assert
        assertThat(requireNonNull(response.getBody()).items())
                .extracting(SellerProductView::registeredTimeUtc)
                .isSortedAccordingTo(reverseOrder());
    }

}

