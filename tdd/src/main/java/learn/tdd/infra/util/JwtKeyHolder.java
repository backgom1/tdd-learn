package learn.tdd.infra.util;

import javax.crypto.SecretKey;

public record JwtKeyHolder(SecretKey key) {
}
