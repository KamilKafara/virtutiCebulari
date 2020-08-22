package pl.promotion.finder.feature.slackbot;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.IOException;

@Service
public class SlackMessageSender {
    public void sendPromotionMessage(ProductDTO productDTO) throws IOException, SlackApiException {
        String token = "";
        Slack slack = Slack.getInstance();
        ChatPostMessageResponse response = slack.methods(token)
                .chatPostMessage(req -> req
                        .channel("#general")
                        .unfurlLinks(true)
                        .unfurlMedia(true)
                        .linkNames(true)
                        .text(":wave: " + productDTO.toString()));
    }

}
