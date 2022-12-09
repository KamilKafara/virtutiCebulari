package pl.promotion.finder.feature.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import pl.promotion.finder.feature.exchange.rates.utils.ExchangeDTO;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Log4j2
@ToString
@Validated
@Setter
@Getter
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExtendedProductDTO extends ProductDTO {
    @NotNull
    private ExchangeDTO exchangeOldPrice;
    @NotNull
    private ExchangeDTO exchangedNewPrice;

    public ExtendedProductDTO(ProductDTO productDTO, ExchangeDTO exchangeOldPrice, ExchangeDTO exchangedNewPrice) {
        super(productDTO.getId(),
                productDTO.getShopName(),
                productDTO.getProductUrl(),
                productDTO.getProductName(),
                productDTO.getOldPrice(),
                productDTO.getNewPrice(),
                productDTO.getAmount(),
                productDTO.getPictureUrl(),
                productDTO.getPercentageCut(),
                productDTO.getCreateDate(),
                productDTO.getShop());
        this.exchangedNewPrice = exchangedNewPrice;
        this.exchangeOldPrice = exchangeOldPrice;

    }
}
