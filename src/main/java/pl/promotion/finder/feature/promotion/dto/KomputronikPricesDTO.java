package pl.promotion.finder.feature.promotion.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KomputronikPricesDTO {
    private boolean has_promotion;
    private String price_base_gross;
    private String price_base_net;
    private String price_gross;
    private String price_net;
    private Long promotion_percentage;
    private Long product_id;
    private String price_currency;
    private String discount_currency;
    private String discount;
    private String base_price;
    private String final_price;
}
