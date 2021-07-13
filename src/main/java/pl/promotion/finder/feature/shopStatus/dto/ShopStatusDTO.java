package pl.promotion.finder.feature.shopStatus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import pl.promotion.finder.feature.shop.repository.ShopEntity;

import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@Getter
public class ShopStatusDTO {
    private Long id;
    private ShopEntity shop;
    private Date lastStatus;
    private Date lastWorkedStatus;
    private boolean enable;
}
