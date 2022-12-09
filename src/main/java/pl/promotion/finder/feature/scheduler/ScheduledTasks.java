package pl.promotion.finder.feature.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.service.ProductService;
import pl.promotion.finder.feature.promotion.service.PromotionService;
import pl.promotion.finder.feature.slackbot.SlackMessageSender;

import java.util.List;

@Log4j2
@Component
@Service
@AllArgsConstructor
public class ScheduledTasks {
    private static final int DURATION = 60_000;
    private final SlackMessageSender slackMessageSender;
    private final ProductService productService;

    private final PromotionService promotionService;

    @Scheduled(fixedRate = DURATION)
    public void reportPromotion() {
        promotionService.getDailyPromotion(false)
                .forEach(this::checkNewPromotion);
    }

    private void checkNewPromotion(ProductDTO productDTO) {
        if (productDTO != null) {
            List<ProductDTO> productsFromJPA = productService.findProductsByInterval(productDTO.getProductName());
            if (productsFromJPA.isEmpty()) {
                sendMessage(productDTO);
            }
        }
    }

    private void sendMessage(ProductDTO promotionToSend) {
        log.info("Send message to slack: " + promotionToSend.toString());

        slackMessageSender.sendPromotionMessage(promotionToSend);
        promotionToSend.setShop(promotionToSend.getShop());
        productService.save(promotionToSend);

        log.info("Response body for " + promotionToSend.getShop().getName() + " : " + promotionToSend);
    }
}
