package pl.promotion.finder.feature.slackbot;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import org.springframework.stereotype.Service;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.exception.NotFoundException;
import pl.promotion.finder.feature.slackbot.dto.SupportMessageDTO;

import java.io.IOException;

import static com.github.seratch.jslack.api.model.block.Blocks.asBlocks;
import static com.github.seratch.jslack.api.webhook.WebhookPayloads.payload;

@Service
public class SupportMessageSender {
    private static final String SLACK_HOOKS_URL = "https://hooks.slack.com/services/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";
    private static final boolean IS_SUPPORT_ENABLE = true;
    private static final String SUBMIT_BUTTON_MESSAGE = "Go to implementation";

    public void registerAnnouncement(SupportMessageDTO supportMessageDTO) {
        if (IS_SUPPORT_ENABLE) {
            try {
                Slack slack = Slack.getInstance();
                slack.send(SLACK_HOOKS_URL, setupPayload(supportMessageDTO));
            } catch (IOException e) {
                throw new NotFoundException("Not found slack hooks url. Check your environment variables",
                        new FieldInfo("SLACK_SUPPORT_HOOKS_URL", ErrorCode.NOT_FOUND_PROPERTY));
            }
        }
    }

    private Payload setupPayload(SupportMessageDTO supportMessageDTO) {
        return payload(b -> b.blocks(
                asBlocks(
                        PayloadBuilder.buildSectionBlock(supportMessageDTO.getMessage()),
                        PayloadBuilder.buildSectionBlock(supportMessageDTO.getShopName()),
                        PayloadBuilder.buildActionsBlock(supportMessageDTO.getPath(), SUBMIT_BUTTON_MESSAGE)

                )).text(supportMessageDTO.getShopName()));
    }
}
