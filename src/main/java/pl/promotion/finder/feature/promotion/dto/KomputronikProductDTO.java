package pl.promotion.finder.feature.promotion.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KomputronikProductDTO {
    private KomputronikPricesDTO prices;
    private String alternative_image_url;
    private String name;
    private String url;
    private Long id;
    private String code;
    private String is_limited_sale;
    private String ordered_quantity;
    private String available_quantity;
    private String date_to_end_promotion;
    private KomputronikDescriptionDTO description;
    private boolean is_buyable;
    private KomputronikLimitedSaleDTO limited_sale;
    private boolean has_promotions_to_layer;
    private String object_type;
}
