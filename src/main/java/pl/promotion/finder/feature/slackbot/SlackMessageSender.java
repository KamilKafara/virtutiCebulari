package pl.promotion.finder.feature.slackbot;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.model.block.ActionsBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.TextObject;
import com.github.seratch.jslack.api.model.block.element.ImageElement;
import com.github.seratch.jslack.api.webhook.Payload;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

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
    public void sendPromotionMessage(ProductDTO productDTO) throws IOException {
        String url = "https://hooks.slack.com/services/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";
        Slack slack = Slack.getInstance();
        slack.send(url, setupPayload(productDTO));
    }

    private Payload setupPayload(ProductDTO productDTO) {
        SectionBlock productNameWithImageBlock = SectionBlock.builder()
                .text(plainText(productDTO.getProductName()))
                .accessory(ImageElement.builder()
                        .imageUrl(productDTO.getPictureUrl())
                        .altText(productDTO.getProductName()).build())
                .build();

        List<TextObject> priceFields = new ArrayList<>();
        priceFields.add(markdownText("~" + productDTO.getOldPrice() + "~"));
        priceFields.add(markdownText("*" + productDTO.getNewPrice() + "*"));

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
