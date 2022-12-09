package pl.promotion.finder.feature.exchange.rates.utils;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * current and historic exchange rates of foreign currencies:
 */
@AllArgsConstructor
public enum ExchangeType {
    /**
     * table A of middle exchange rates of foreign currencies,
     */
    A(1),
    /**
     * table B of middle exchange rates of foreign currencies,
     */
    B(2),
    /**
     * able C of buy and sell prices of foreign currencies;
     */
    C(3);

    public static final Map<String, ExchangeType> TYPES = new HashMap<>();

    static {
        Arrays.stream(ExchangeType.values())
                .forEach(type -> TYPES.put(type.name(), type));
    }

    private final int id;
}
