package learn.tdd.infra.util;


public class UserPropertyValidator {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]*$";

    public static boolean validPassword(String password) {
        return password == null || password.isBlank() || password.length() < 8;
    }

    public static boolean validUsername(String username) {
        return username == null || username.isBlank() || username.length() < 3 || !username.matches(USERNAME_REGEX);
    }

    public static boolean validEmail(String email) {
        return email == null || !email.contains("@") || email.endsWith("@") || !email.matches(EMAIL_REGEX);
    }

}
