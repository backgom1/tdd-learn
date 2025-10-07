package learn.tdd.view;

import java.util.UUID;

public record SellerMeView(UUID id, String email, String username, String contactEmail) {
}
