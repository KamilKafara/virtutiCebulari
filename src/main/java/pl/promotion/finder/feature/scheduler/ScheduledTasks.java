package pl.promotion.finder.feature.scheduler;

import com.slack.api.methods.SlackApiException;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;
import pl.promotion.finder.feature.shop.dto.Shop;
import pl.promotion.finder.feature.shop.service.*;
import pl.promotion.finder.feature.slackbot.SlackMessageSender;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@Log4j2
@Component
@Service
public class ScheduledTasks {
    private static final int DURATION = 10_000;
    private final SlackMessageSender slackMessageSender;
    private final AmsoService amsoService;
    private final CarinetService carinetService;
    private final CombatService combatService;
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private HashMap<Shop, ProductDTO> oldPromotions;
    private HashMap<Shop, ProductDTO> newPromotions;

    public ScheduledTasks(SlackMessageSender slackMessageSender, AmsoService amsoService, CarinetService carinetService, CombatService combatService, MoreleService moreleService, XkomService xkomService) {
        this.slackMessageSender = slackMessageSender;
        this.amsoService = amsoService;
        this.carinetService = carinetService;
        this.combatService = combatService;
        this.moreleService = moreleService;
        this.xkomService = xkomService;
        clearPromotions();
    }

    protected void clearPromotions() {
        HashMap<Shop, ProductDTO> oldPromotions = new HashMap<>();
        oldPromotions.put(Shop.ALTO, null);
        oldPromotions.put(Shop.AMSO, null);
        oldPromotions.put(Shop.CARINET, null);
        oldPromotions.put(Shop.COMBAT, null);
        oldPromotions.put(Shop.MORELE, null);
        oldPromotions.put(Shop.XKOM, null);
        this.oldPromotions = oldPromotions;

        HashMap<Shop, ProductDTO> newPromotions = new HashMap<>();
        newPromotions.put(Shop.ALTO, null);
        newPromotions.put(Shop.AMSO, null);
        newPromotions.put(Shop.CARINET, null);
        newPromotions.put(Shop.COMBAT, null);
        newPromotions.put(Shop.MORELE, null);
        newPromotions.put(Shop.XKOM, null);
        this.newPromotions = newPromotions;
    }

    @Scheduled(fixedRate = DURATION)
    public void reportPromotion() throws IOException, SlackApiException {
        log.info("Promotion scheduler: {}", new Date().toString());
        checkNewPromotion(amsoService, Shop.AMSO);
        checkNewPromotion(carinetService, Shop.CARINET);
        checkNewPromotion(combatService, Shop.COMBAT);
        checkNewPromotion(moreleService, Shop.MORELE);
        checkNewPromotion(xkomService, Shop.XKOM);
    }

    private void checkNewPromotion(Promotion promotionService, Shop shop) throws IOException, SlackApiException {
        newPromotions.put(shop, promotionService.getPromotion());
        if (oldPromotions.get(shop) != null) {
            if (!oldPromotions.get(shop).getProductName().equals(newPromotions.get(shop).getProductName())) {
                log.info("Send message to slack");
                slackMessageSender.sendPromotionMessage(newPromotions.get(shop));
                log.info("Response body for " + shop.toString() + " : " + newPromotions.get(shop));
            }
        } else {
            oldPromotions.put(shop, newPromotions.get(shop));
        }
    }
}


