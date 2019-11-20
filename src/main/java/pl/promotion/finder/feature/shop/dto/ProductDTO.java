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
    private String oldPrice;
    private String newPrice;
    private String amount;
    private Date expirationDate;
    private String shopName;
    private String pictureUrl;
    private String productUrl;
}
