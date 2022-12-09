package pl.promotion.finder.feature.promotion.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.exchange.rates.ExchangeApi;
import pl.promotion.finder.feature.exchange.rates.utils.ExchangeDTO;
import pl.promotion.finder.feature.exchange.rates.utils.ExchangeType;
import pl.promotion.finder.feature.product.dto.ExtendedProductDTO;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.shop.dto.Shop;
import pl.promotion.finder.utils.PriceMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.promotion.finder.feature.exchange.rates.utils.CurrencyType.EUR;
import static pl.promotion.finder.feature.exchange.rates.utils.CurrencyType.PLN;

@Service
@AllArgsConstructor
@Log4j2
public class PromotionService {
    private final AmsoService amsoService;
    private final CombatService combatService;
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private final KomputronikService komputronikService;

    private final ExchangeApi exchangeApi;

    public List<ProductDTO> getDailyPromotion(boolean withExchangeRate) {
        Set<ProductDTO> productDTOList = new HashSet<>();

        productDTOList.add(amsoService.getPromotion());
        productDTOList.add(combatService.getPromotion());
        productDTOList.add(moreleService.getPromotion());
        productDTOList.add(xkomService.getPromotion());
        productDTOList.add(komputronikService.getPromotion());

        List<ProductDTO> dtoList = productDTOList.parallelStream()
                .filter(ProductDTO::isFilled)
                .collect(Collectors.toList());
        if (!withExchangeRate) {
            return dtoList;
        }
        return getProductPriceInForeignCurrencies(dtoList);
    }

    @NotNull
    private List<ProductDTO> getProductPriceInForeignCurrencies(List<ProductDTO> dtoList) {
        List<ProductDTO> withExchange = new ArrayList<>();
        for (ProductDTO item : dtoList) {
            try {
                ExchangeDTO oldPriceExchangeDTO = exchangeApi.exchangeRate(ExchangeType.A, PLN, EUR, PriceMapper.priceFactory(item.getOldPrice()));
                ExchangeDTO newPriceExchangeDTO = exchangeApi.exchangeRate(ExchangeType.A, PLN, EUR, PriceMapper.priceFactory(item.getNewPrice()));
                withExchange.add(new ExtendedProductDTO(item, oldPriceExchangeDTO, newPriceExchangeDTO));
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
        return withExchange;
    }

    public Shop[] getAllEndpoints() {
        return Shop.values();
    }
}
