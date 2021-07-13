package pl.promotion.finder.feature.slackbot;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.model.block.ActionsBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.TextObject;
import com.github.seratch.jslack.api.model.block.element.ImageElement;
import com.github.seratch.jslack.api.webhook.Payload;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.exception.NotFoundException;
import pl.promotion.finder.feature.product.dto.PriceMapper;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.service.ProductService;
import pl.promotion.finder.feature.slackbot.dto.PayloadFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.seratch.jslack.api.model.block.Blocks.asBlocks;
import static com.github.seratch.jslack.api.model.block.composition.BlockCompositions.markdownText;
import static com.github.seratch.jslack.api.model.block.composition.BlockCompositions.plainText;
import static com.github.seratch.jslack.api.webhook.WebhookPayloads.payload;

@Log4j2
@Service
public class SlackMessageSender {

    private static final String SLACK_HOOKS_URL = System.getenv("SLACK_WEBHOOK");
    private static final String GO_TO_PROMOTION = "Przejdź do promocji";
    private static final String SALE_MESSAGE = "Obniżka  -";
    private final ProductService productService;

    public SlackMessageSender(ProductService productService) {
        this.productService = productService;
    }

    public String sendPromotionMessage(String message) {
        Slack slack = Slack.getInstance();
        String payload = PayloadFormat.buildPayload(message);
        try {
            slack.send(SLACK_HOOKS_URL, payload);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return payload;
    }

    public void sendPromotionMessage(ProductDTO productDTO) {
        Slack slack = Slack.getInstance();
        try {
            slack.send(SLACK_HOOKS_URL, setupPayload(productDTO));
        } catch (IOException e) {
            throw new NotFoundException("Not found slack hooks url. Check your environment variables", new FieldInfo("SLACK_HOOKS_URL", ErrorCode.NOT_FOUND_PROPERTY));
        }
    }

    private Payload setupPayload(ProductDTO productDTO) {
        SectionBlock productNameWithImageBlock = SectionBlock.builder()
                .text(plainText(productDTO.getProductName()))
                .accessory(ImageElement.builder()
                        .imageUrl(productDTO.getPictureUrl())
                        .altText(productDTO.getProductName()).build())
                .build();

        List<TextObject> priceFields = setupPrice(productDTO);

        addAnnotationWithLowerPrice(priceFields, productDTO);

        SectionBlock shopNameSectionBlock = PayloadBuilder.buildSectionBlock(productDTO.getShopName());
        SectionBlock priceSectionBlock = PayloadBuilder.buildSectionBlock(priceFields);
        ActionsBlock productURLActionBlock = PayloadBuilder.buildActionsBlock(productDTO.getProductUrl(), GO_TO_PROMOTION);

        Payload payload = payload(b -> b.blocks(
                asBlocks(
                        productNameWithImageBlock,
                        shopNameSectionBlock,
                        priceSectionBlock,
                        productURLActionBlock
                )));
        payload.setText(productDTO.getProductName());

        return payload;
    }

    private List<TextObject> setupPrice(ProductDTO productDTO) {
        List<TextObject> priceFields = new ArrayList<>();
        if (!productDTO.getNewPrice().equals(productDTO.getOldPrice())) {
            priceFields.add(markdownText("~" + productDTO.getOldPrice() + "~"));
        }
        priceFields.add(markdownText("*" + productDTO.getNewPrice() + "*"));

        productDTO.setupPercentageCut();
        if (productDTO.getPercentageCut() != null) {
            priceFields.add(markdownText("*" + SALE_MESSAGE + productDTO.getPercentageCut() + "% " + "*"));
        }
        return priceFields;
    }

    private void addAnnotationWithLowerPrice(List<TextObject> priceFields, ProductDTO productDTO) {
        Optional<ProductDTO> productWithLowerPrice = Optional.ofNullable(productService.getProductByNameWithLowerPrice(productDTO.getProductName()));
        if (productWithLowerPrice.isPresent()) {
            try {
                PriceMapper.priceFactory(productWithLowerPrice.get().getNewPrice());
                PriceMapper.priceFactory(productDTO.getNewPrice());

                double lowerPrice = PriceMapper.getDecimalPrice().doubleValue();
                double currentProductPrice = PriceMapper.getDecimalPrice().doubleValue();

                if (lowerPrice < currentProductPrice) {
                    priceFields.addAll(buildAnnotation(productWithLowerPrice.get()));
                }
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
        }
    }

    private List<TextObject> buildAnnotation(ProductDTO productWithLowerPrice) {
        List<TextObject> annotation = new ArrayList<>();
        annotation.add(markdownText("Ten produkt już kosztował mniej - " + productWithLowerPrice.getNewPrice()));
        annotation.add(markdownText("Korzystniejsza promocja już była: " + productWithLowerPrice.getCreateDate()));
        annotation.add(markdownText("Zastanów się dwa razy zanim zdecydujesz się na zakup."));
        return annotation;
    }
}
