package pl.promotion.finder.feature.promotion.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CombatDTO {
    private Boolean isActive;
    private Boolean available;
    private String title;
    private String promotionId;
    private String discount;
    private String regularPrice;
    private String promoPrice;
    private String left;
    private String sold;
    private String customerLimit;
    private String regularUrl;
    private String productUrl;
    private String photo;
    private String percent;
    private String total;
}
