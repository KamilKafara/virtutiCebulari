package pl.promotion.finder.feature.exchange.rates;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.exchange.rates.utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ExchangeApiImpl implements ExchangeApi {
    private static final String API_URL = "https://api.nbp.pl/api/exchangerates/tables/";
    private static final int DEFAULT_SCALE = 2;

    public List<TableDTO> getCurrencyByType(ExchangeType type) throws IOException {
        String data = fetchData(type);
        final ObjectMapper objectMapper = new ObjectMapper();
        TableDTO[] array = objectMapper.readValue(data, TableDTO[].class);
        return List.of(array);
    }

    private String fetchData(ExchangeType type) throws IOException {
        URL url = new URL(API_URL + type.name());
        URLConnection urlConnection = url.openConnection();
        urlConnection.setUseCaches(true);
        InputStream inputFile = urlConnection.getInputStream();
        return new String(inputFile.readAllBytes(), StandardCharsets.UTF_8);
    }

    public CurrencyDTO getCurrencyByType(ExchangeType type, CurrencyType currencyType) throws IOException {
        List<TableDTO> list = getCurrencyByType(type);
        Optional<TableDTO> item = list.stream().findFirst();
        if (item.isEmpty()) {
            log.warn("Not found any data");
            return CurrencyDTO.initDefaultCurrency();
        }
        if (CurrencyType.PLN.test(currencyType)) {
            return CurrencyDTO.initDefaultCurrency();
        }
        Optional<CurrencyDTO> currencyDTOS = item.get().getRates().stream()
                .filter(it -> it.getCode().equalsIgnoreCase(currencyType.name()))
                .findFirst();
        if (currencyDTOS.isEmpty()) {
            log.warn("Not data with this currencyType {" + currencyType.name() + "}.");
            return CurrencyDTO.initDefaultCurrency();
        }
        return currencyDTOS.get();
    }

    @Override
    public ExchangeDTO exchangeRate(ExchangeType type,
                                    CurrencyType source,
                                    CurrencyType target,
                                    BigDecimal basicValue) throws IOException {
        BigDecimal targetValue = BigDecimal.ONE;
        BigDecimal sourceValue = BigDecimal.ONE;
        if (!CurrencyType.PLN.test(source)) {
            sourceValue = getCurrencyByType(type, source).getMid();
        }
        if (!CurrencyType.PLN.test(target)) {
            targetValue = getCurrencyByType(type, target).getMid();
        }
        BigDecimal exchangeRate = sourceValue.divide(targetValue, DEFAULT_SCALE, RoundingMode.UP);
        BigDecimal result = exchangeRate.multiply(basicValue);
        return ExchangeDTO.builder()
                .exchangeType(type)
                .sourceValue(basicValue)
                .targetValue(result)
                .currencySource(source)
                .currencyTarget(target).build();

    }
}
