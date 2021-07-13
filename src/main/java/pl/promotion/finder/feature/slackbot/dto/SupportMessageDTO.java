package pl.promotion.finder.feature.slackbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.modelmapper.spi.ErrorMessage;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@Getter
public class SupportMessageDTO {
    private final String shopName;
    private final String message;
    private final String path;
    private List<ErrorMessage> errorMessages;
}
