package pl.promotion.finder.feature.shop.dto;

import lombok.*;

@Setter
@Getter
@ToString
public class ProductDTO {
    private final String shopName;

    private String productUrl;
    private String productName;
    private String oldPrice;
    private String newPrice;
    private String amount;
    private String pictureUrl;

    public ProductDTO(String shopName, String productUrl) {
        this.shopName = shopName;
        this.productUrl = productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
}
