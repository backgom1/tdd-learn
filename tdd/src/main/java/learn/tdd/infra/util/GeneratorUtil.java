package learn.tdd.infra.util;

import java.security.SecureRandom;
import java.util.UUID;

public class GeneratorUtil {

    private static final SecureRandom random = new SecureRandom();

    public static String generateEmail() {
        return UUID.randomUUID() + "@test.com";
    }

    public static String generateUsername() {
        return UUID.randomUUID().toString();
    }

    public static String generatePassword() {
        StringBuilder mixture = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            mixture.append((char) (random.nextInt('A', 'Z' + 1)));
            mixture.append((char) (random.nextInt('0', '9' + 1)));
            mixture.append((char) (random.nextInt('a', 'z' + 1)));
        }
        return "password" + mixture;
    }


}
