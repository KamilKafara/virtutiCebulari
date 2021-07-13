package pl.promotion.finder.feature.slackbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Builder
@Getter
public class SupportMessageDTO {
    private final String message;
    private final String shopName;
    private final String path;
}
