package learn.tdd.infra.util;

import java.util.UUID;

public class GeneratorUtil {

    public static String generateEmail() {
        return UUID.randomUUID() + "@test.com";
    }

    public static String generateUsername() {
        return UUID.randomUUID().toString();
    }

    public static String generatePassword() {
        return "password" + UUID.randomUUID().toString();
    }



}
