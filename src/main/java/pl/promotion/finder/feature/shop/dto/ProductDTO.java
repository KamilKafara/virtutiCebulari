package pl.promotion.finder.feature.shop.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Data
@ToString
@NoArgsConstructor
public class ProductDTO {
    private int id;
    private String productName;
    @NonNull
    private String oldPrice;
    @NonNull
    private String newPrice;
    private String amount;
    @NonNull
    private String shopName;
    @NonNull
    private String pictureUrl;
    @NonNull
    private String productUrl;
}
