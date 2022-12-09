package pl.promotion.finder.feature.exchange.rates.utils;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeDTO {
    private ExchangeType exchangeType;
    private CurrencyType currencySource;
    private BigDecimal sourceValue;
    private CurrencyType currencyTarget;
    private BigDecimal targetValue;
}
