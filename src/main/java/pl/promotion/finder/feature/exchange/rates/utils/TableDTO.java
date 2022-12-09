package pl.promotion.finder.feature.exchange.rates.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TableDTO {
    private ExchangeType table;
    private String no;
    private String effectiveDate;
    private List<CurrencyDTO> rates;
}
