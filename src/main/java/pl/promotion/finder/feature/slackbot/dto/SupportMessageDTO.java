package pl.promotion.finder.feature.slackbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SupportMessageDTO {
    private final String message;
    private final String shopName;
    private final String path;
}
