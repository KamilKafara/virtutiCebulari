package pl.promotion.finder.feature.slackbot;

import pl.promotion.finder.feature.shopStatus.dto.ShopStatusDTO;
import pl.promotion.finder.feature.slackbot.dto.SupportMessageDTO;

import java.util.ArrayList;

public class SupportMessageMapper {

    public SupportMessageDTO toDto(ShopStatusDTO shopStatusDTO) {
        return SupportMessageDTO.builder()
                .path("loremIpsum")
                .shopName(shopStatusDTO.getShop().getName())
                .errorMessages(new ArrayList<>())
                .build();
    }
}
