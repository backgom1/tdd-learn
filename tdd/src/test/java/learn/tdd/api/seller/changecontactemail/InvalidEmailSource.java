package learn.tdd.api.seller.changecontactemail;

import org.junit.jupiter.params.provider.MethodSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@MethodSource("learn.tdd.TestDataSource#invalidEmails")
public @interface InvalidEmailSource {
}
