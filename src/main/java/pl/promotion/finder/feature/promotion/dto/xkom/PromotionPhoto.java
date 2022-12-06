package pl.promotion.finder.feature.promotion.dto.xkom;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PromotionPhoto {
    private String Url;
    private String ThumbnailUrl;
    private String UrlTemplate;
}
