package pl.promotion.finder.feature.slackbot;

import com.github.seratch.jslack.api.model.block.ActionsBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.TextObject;

import java.util.List;

import static com.github.seratch.jslack.api.model.block.Blocks.actions;
import static com.github.seratch.jslack.api.model.block.composition.BlockCompositions.plainText;
import static com.github.seratch.jslack.api.model.block.element.BlockElements.asElements;
import static com.github.seratch.jslack.api.model.block.element.BlockElements.button;

public class PayloadBuilder {
    private PayloadBuilder() {
    }

    public static SectionBlock buildSectionBlock(String message) {
        return SectionBlock.builder()
                .text(plainText(message))
                .build();
    }

    public static SectionBlock buildSectionBlock(List<TextObject> fields) {
        return SectionBlock.builder()
                .fields(fields)
                .build();
    }

    public static SectionBlock buildSectionBlock(String message, List<TextObject> fields) {
        return SectionBlock.builder()
                .text(plainText(message))
                .fields(fields)
                .build();
    }

    public static ActionsBlock buildActionsBlock(String url, String textMessage) {
        return actions(
                a -> a.elements(asElements(button(btn ->
                        btn.text(plainText(pt -> pt.emoji(true)
                                .text(textMessage)))
                                .url(url)))));
    }
}
