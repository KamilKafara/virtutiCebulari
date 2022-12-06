package pl.promotion.finder.feature.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.service.ProductService;
import pl.promotion.finder.feature.promotion.service.*;
import pl.promotion.finder.feature.shop.dto.Shop;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.slackbot.SlackMessageSender;

import java.io.IOException;
import java.util.List;

@Log4j2
@Component
@Service
@AllArgsConstructor
public class ScheduledTasks {
    private static final int DURATION = 60_000;
    private final SlackMessageSender slackMessageSender;
    private final AmsoService amsoService;
    private final CarinetService carinetService;
    private final CombatService combatService;
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private final ApolloService apolloService;
    private final ProductService productService;
    private final KomputronikService komputronikService;
    private final SonyCentreService sonyCentreService;
    private final AllWeldService allWeldService;

    @Scheduled(fixedRate = DURATION)
    public void reportPromotion() {
//        checkNewPromotion(amsoService, Shop.AMSO);
//        checkNewPromotion(carinetService, Shop.CARINET);
//        checkNewPromotion(moreleService, Shop.MORELE);
        checkNewPromotion(xkomService, Shop.XKOM);
//        checkNewPromotion(apolloService, Shop.APOLLO);
//        checkNewPromotion(combatService, Shop.COMBAT);
//        checkNewPromotion(komputronikService, Shop.KOMPUTRONIK);
//        checkNewPromotion(sonyCentreService, Shop.SONY_CENTRE);
//        checkNewPromotion(allWeldService, Shop.ALL_WELD);
    }

    private void checkNewPromotion(Promotion promotionService, Shop shop) {
        ProductDTO productDTO = null;
        try {
            productDTO = promotionService.getPromotion();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        if (productDTO != null) {
            List<ProductDTO> productsFromJPA = productService.findProductsByInterval(productDTO.getProductName());
            if (productsFromJPA.isEmpty()) {
                sendMessage(productDTO, shop);
            }
        }
    }

    private void sendMessage(ProductDTO promotionToSend, Shop shop) {
        log.info("Send message to slack: " + promotionToSend.toString());

//        slackMessageSender.sendPromotionMessage(promotionToSend);
        promotionToSend.setShop(new ShopDTO(shop.name().toLowerCase()));
        productService.save(promotionToSend);

        log.info("Response body for " + shop.toString() + " : " + promotionToSend);
    }
}
