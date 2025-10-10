package learn.tdd.result;

public record PageCarrier<T>(T[] items, String continuationToken) {
}
