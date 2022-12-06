package pl.promotion.finder.feature.promotion.dto.xkom;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductXkom {
    private List<PromotionPhoto> Photos;
}
