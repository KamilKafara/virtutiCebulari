package pl.promotion.finder.feature.shop.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Log4j2
@Service
public class CombatService implements Promotion {
    private static final String hotShotTag = "div#hotShot";
    private static final String amountTag = "pull-left";
    private static final String newPriceTag = "new-price";
    private static final String oldPriceTag = "old-price";
    private static final String productNameTag = "product-name";
    private static final String productImageTag = "img-responsive center-block";
    private static final String shopName = "combat";
    private static final String productURL = "https://www.combat.pl/";

    private ProductDTO getCombatProduct(Document document) {
        return new ProductDTO(shopName, productURL);
    }

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            URL urlCombat = new URL(productURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCombat.openStream()));
            String inputLine;
            StringBuilder combatSB = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                combatSB.append(inputLine);
            }
            Document combatDocument = Jsoup.parse(combatSB.toString());
            return getCombatProduct(combatDocument);
        } catch (NullPointerException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }
}
