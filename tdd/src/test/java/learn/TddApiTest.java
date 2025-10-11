package learn;

import learn.tdd.TddApplication;
import learn.tdd.api.PasswordEncoderConfiguration;
import learn.tdd.api.TestFixtureConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
        classes = {TddApplication.class, TestFixtureConfiguration.class, PasswordEncoderConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public @interface TddApiTest {
}
