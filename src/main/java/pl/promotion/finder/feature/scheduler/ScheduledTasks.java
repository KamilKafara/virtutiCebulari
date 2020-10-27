package pl.promotion.finder.feature.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;
import pl.promotion.finder.feature.shop.dto.Shop;
import pl.promotion.finder.feature.shop.service.*;
import pl.promotion.finder.feature.slackbot.SlackMessageSender;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Optional;

@Log4j2
@Component
@Service
public class ScheduledTasks {
    private static final int DURATION = 15_000;
    private final SlackMessageSender slackMessageSender;
    private final AmsoService amsoService;
    private final CarinetService carinetService;
    private final CombatService combatService;
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private final ZadowolenieService zadowolenieService;
    private final VobisService vobisService;
    private final ApolloService apolloService;

    private EnumMap<Shop, ProductDTO> oldPromotions;
    private EnumMap<Shop, ProductDTO> newPromotions;

    public ScheduledTasks(SlackMessageSender slackMessageSender, AmsoService amsoService, CarinetService carinetService, CombatService combatService, MoreleService moreleService, XkomService xkomService, ZadowolenieService zadowolenieService, VobisService vobisService, ApolloService apolloService) {
        this.slackMessageSender = slackMessageSender;
        this.amsoService = amsoService;
        this.carinetService = carinetService;
        this.combatService = combatService;
        this.moreleService = moreleService;
        this.xkomService = xkomService;
        this.zadowolenieService = zadowolenieService;
        this.vobisService = vobisService;
        this.apolloService = apolloService;
        clearPromotions();
    }

    protected void clearPromotions() {
        this.oldPromotions = setNullPromotions();
        this.newPromotions = setNullPromotions();
    }

    protected EnumMap<Shop, ProductDTO> setNullPromotions() {
        EnumMap<Shop, ProductDTO> promotions = new EnumMap<>(Shop.class);
        promotions.put(Shop.ALTO, null);
        promotions.put(Shop.APOLLO, null);
        promotions.put(Shop.AMSO, null);
        promotions.put(Shop.CARINET, null);
        promotions.put(Shop.COMBAT, null);
        promotions.put(Shop.MORELE, null);
        promotions.put(Shop.XKOM, null);
        promotions.put(Shop.VOBIS, null);
        promotions.put(Shop.ZADOWOLENIE, null);
        return promotions;
    }

    @Scheduled(fixedRate = DURATION)
    public void reportPromotion() throws IOException {
        checkNewPromotion(amsoService, Shop.AMSO);
        checkNewPromotion(carinetService, Shop.CARINET);
        checkNewPromotion(combatService, Shop.COMBAT);
        checkNewPromotion(moreleService, Shop.MORELE);
        checkNewPromotion(xkomService, Shop.XKOM);
        checkNewPromotion(zadowolenieService, Shop.ZADOWOLENIE);
        checkNewPromotion(vobisService, Shop.VOBIS);
        checkNewPromotion(apolloService, Shop.APOLLO);
    }

    private void checkNewPromotion(Promotion promotionService, Shop shop) throws IOException {
        newPromotions.put(shop, promotionService.getPromotion());
        if (oldPromotions.get(shop) != null) {
            Optional<ProductDTO> optionalProductDTO = Optional.ofNullable(newPromotions.get(shop));
            if (optionalProductDTO.isPresent()) {
                ProductDTO promotionToSend = optionalProductDTO.get();
                log.info(shop.toString() + " - " + promotionToSend.getProductName());
                if (!oldPromotions.get(shop).getProductName().equals(promotionToSend.getProductName())) {
                    log.info("Send message to slack");
                    log.info(promotionToSend.toString());
                    slackMessageSender.sendPromotionMessage(promotionToSend);
                    log.info("Response body for " + shop.toString() + " : " + promotionToSend);
                    newPromotions.put(shop, promotionToSend);
                    oldPromotions.put(shop, promotionToSend);
                }
            }
        } else {
            oldPromotions.put(shop, newPromotions.get(shop));
        }
    }
}


