package pl.promotion.finder.feature.slackbot;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.model.block.ActionsBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.webhook.Payload;
import lombok.AllArgsConstructor;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.exception.NotFoundException;
import pl.promotion.finder.feature.slackbot.dto.SupportMessageDTO;

import java.io.IOException;

import static com.github.seratch.jslack.api.model.block.Blocks.actions;
import static com.github.seratch.jslack.api.model.block.Blocks.asBlocks;
import static com.github.seratch.jslack.api.model.block.composition.BlockCompositions.plainText;
import static com.github.seratch.jslack.api.model.block.element.BlockElements.asElements;
import static com.github.seratch.jslack.api.model.block.element.BlockElements.button;
import static com.github.seratch.jslack.api.webhook.WebhookPayloads.payload;

@AllArgsConstructor
public class SupportChannel {
    private static final String SLACK_HOOKS_URL = System.getenv("SLACK_SUPPORT_WEBHOOK");
    private static final boolean IS_SUPPORT_ENABLE = Boolean.parseBoolean(System.getenv("SUPPORT_ENABLE"));
    private static final String SUBMIT_BUTTON_MESSAGE = "Go to implementation";

    private final String message;

    public void registerAnnouncement(SupportMessageDTO supportMessageDTO) {
        if (IS_SUPPORT_ENABLE) {
            try {
                Slack slack = Slack.getInstance();
                slack.send(SLACK_HOOKS_URL, setupPayload(supportMessageDTO));
            } catch (IOException e) {
                throw new NotFoundException("Not found slack hooks url. Check your environment variables", new FieldInfo("SLACK_SUPPORT_HOOKS_URL", ErrorCode.NOT_FOUND_PROPERTY));
            }
        }
    }

    private Payload setupPayload(SupportMessageDTO supportMessageDTO) {
        SectionBlock productNameWithImageBlock = SectionBlock.builder()
                .text(plainText(supportMessageDTO.getMessage()))
                .build();

        SectionBlock shopNameSectionBlock = SectionBlock.builder()
                .text(plainText(supportMessageDTO.getShopName()))
                .build();

        ActionsBlock productURLActionBlock = actions(
                a -> a.elements(asElements(button(btn ->
                        btn.text(plainText(pt -> pt.emoji(true)
                                .text(SUBMIT_BUTTON_MESSAGE)))
                                .url(supportMessageDTO.getPath())))));

        Payload payload = payload(b -> b.blocks(
                asBlocks(
                        productNameWithImageBlock,
                        shopNameSectionBlock,
                        productURLActionBlock
                )));
        payload.setText(supportMessageDTO.getShopName());

        return payload;
    }

}
