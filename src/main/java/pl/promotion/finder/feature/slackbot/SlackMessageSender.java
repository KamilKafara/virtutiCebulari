package pl.promotion.finder.feature.slackbot;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.model.block.ActionsBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.TextObject;
import com.github.seratch.jslack.api.model.block.element.ImageElement;
import com.github.seratch.jslack.api.webhook.Payload;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.seratch.jslack.api.model.block.Blocks.actions;
import static com.github.seratch.jslack.api.model.block.Blocks.asBlocks;
import static com.github.seratch.jslack.api.model.block.composition.BlockCompositions.markdownText;
import static com.github.seratch.jslack.api.model.block.composition.BlockCompositions.plainText;
import static com.github.seratch.jslack.api.model.block.element.BlockElements.asElements;
import static com.github.seratch.jslack.api.model.block.element.BlockElements.button;
import static com.github.seratch.jslack.api.webhook.WebhookPayloads.payload;

@Service
public class SlackMessageSender {

    private static final String SLACK_HOOKS_URL = System.getenv("SLACK_WEBHOOK");

    public String sendPromotionMessage(String message) throws IOException {
        Slack slack = Slack.getInstance();
        String payload = "{\"text\":\"" + message + "!\"}";
        slack.send(SLACK_HOOKS_URL, payload);
        return payload;
    }

    public void sendPromotionMessage(ProductDTO productDTO) throws IOException {
        Slack slack = Slack.getInstance();
        slack.send(SLACK_HOOKS_URL, setupPayload(productDTO));
    }

    private Payload setupPayload(ProductDTO productDTO) {
        SectionBlock productNameWithImageBlock = SectionBlock.builder()
                .text(plainText(productDTO.getProductName()))
                .accessory(ImageElement.builder()
                        .imageUrl(productDTO.getPictureUrl())
                        .altText(productDTO.getProductName()).build())
                .build();

        List<TextObject> priceFields = new ArrayList<>();
        if (!productDTO.getNewPrice().equals(productDTO.getOldPrice())) {
            priceFields.add(markdownText("~" + productDTO.getOldPrice() + "~"));
        }
        priceFields.add(markdownText("*" + productDTO.getNewPrice() + "*"));
        if (productDTO.getPercentageCut() != null) {
            priceFields.add(markdownText("*" + "Obniżka  -" + productDTO.getPercentageCut() + "% " + "*"));
        }

        SectionBlock shopNameSectionBlock = SectionBlock.builder()
                .text(plainText(productDTO.getShopName()))
                .build();

        SectionBlock priceSectionBlock = SectionBlock.builder()
                .fields(priceFields)
                .build();

        ActionsBlock productURLActionBlock = actions(
                a -> a.elements(asElements(button(btn ->
                        btn.text(plainText(pt -> pt.emoji(true)
                                .text("Przejdź do promocji")))
                                .url(productDTO.getProductUrl())))));

        return payload(b -> b.blocks(
                asBlocks(
                        productNameWithImageBlock,
                        shopNameSectionBlock,
                        priceSectionBlock,
                        productURLActionBlock
                )));
    }

}
