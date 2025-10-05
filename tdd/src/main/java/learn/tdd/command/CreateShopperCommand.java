package learn.tdd.command;

public record CreateShopperCommand(String email, String username, String password) {
}
