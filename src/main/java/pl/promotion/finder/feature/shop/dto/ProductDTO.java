package pl.promotion.finder.feature.shop.dto;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

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
    private Timestamp time;

    public ProductDTO(String shopName, String productUrl) {
        this.shopName = shopName;
        this.productUrl = productUrl;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }


    public String getPictureUrl() {
        if (this.pictureUrl == null || this.getPictureUrl().equals("")) {
            this.setPictureUrl("https://paczaizm.pl/content/wp-content/uploads/elon-musk-za-kazdym-jechanym-razem-partia-razem.jpg");
        }
        return pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equal(shopName, that.shopName) &&
                Objects.equal(productUrl, that.productUrl) &&
                Objects.equal(productName, that.productName) &&
                Objects.equal(oldPrice, that.oldPrice) &&
                Objects.equal(newPrice, that.newPrice) &&
                Objects.equal(amount, that.amount) &&
                Objects.equal(pictureUrl, that.pictureUrl) &&
                Objects.equal(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shopName, productUrl, productName, oldPrice, newPrice, amount, pictureUrl, time);
    }
}
