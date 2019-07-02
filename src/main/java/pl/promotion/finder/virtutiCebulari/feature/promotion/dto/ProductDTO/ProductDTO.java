package pl.promotion.finder.virtutiCebulari.feature.promotion.dto.ProductDTO;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Data
@ToString
@NoArgsConstructor
public class ProductDTO {
    private String productName;
    private Double oldPrice;
    private Double newPrice;
    private Integer amount;
    private Date expirationDate;
    private String shopName;
    private String pictureUrl;
    private String productUrl;
}
