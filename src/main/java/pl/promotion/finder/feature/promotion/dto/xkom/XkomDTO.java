package pl.promotion.finder.feature.promotion.dto.xkom;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class XkomDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Product")
    private ProductXkom product;
    @JsonProperty("Price")
    private BigDecimal price;
    @JsonProperty("OldPrice")
    private BigDecimal oldPrice;
    @JsonProperty("PromotionGainText")
    private String promotionGainText;
    @JsonProperty("PromotionGainTextLine")
    private List<String> promotionGainTextLine;
    @JsonProperty("PromotionGainValue")
    private BigDecimal promotionGainValue;
    @JsonProperty("PromotionTotalCount")
    private BigDecimal promotionTotalCount;
    @JsonProperty("SaleCount")
    private BigDecimal saleCount;
    @JsonProperty("MaxBuyCount")
    private int maxBuyCount;
    @JsonProperty("PromotionName")
    private String promotionName;
    @JsonProperty("PromotionEnd")
    private String promotionEnd;
    @JsonProperty("HtmlContent")
    private String htmlContent;
    @JsonProperty("PromotionPhoto")
    private PromotionPhoto promotionPhoto;
    @JsonProperty("IsActive")
    private boolean isActive;
    @JsonProperty("IsSuspended")
    private boolean isSuspended;
    @JsonProperty("MinimumInstallmentValue")
    private BigDecimal minimumInstallmentValue;
}
