package pl.promotion.finder.feature.promotion.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.product.dto.ProductDTO;

import java.io.IOException;

@Log4j2
@Service
public class CombatService implements Promotion {
    private static final String HOT_SHOT_TAG = "div.product-essential";
    private static final String PRICE_TAG = "span.price";
    private static final String PRODUCT_NAME_TAG = "span.base";
    private static final String PRODUCT_IMAGE_TAG = "div.col-md-10";
    private static final String AMOUNT_VALUE = "empty";
    private static final String SHOP_NAME = "combat";
    private static final String PRODUCT_URL = "https://www.combat.pl/goracy-strzal";

    @Override
    public ProductDTO getPromotion() {
        try {
            Document document = Jsoup.connect(PRODUCT_URL).get();
            return getProduct(document);
        } catch (NullPointerException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    public ProductDTO getProduct(Document document) {
        Elements elements = document.select(HOT_SHOT_TAG);
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, PRODUCT_URL);
        productDTO.setProductName(elements.select(PRODUCT_NAME_TAG).text());
        String oldPrice = elements.select(PRICE_TAG).first().text().replace("zł", "").replace("\\s+", "").replaceAll(" ", "");
        String newPrice = elements.select(PRICE_TAG).last().text().replace("zł", "").replace("\\s+", "").replaceAll(" ", "");

        String productImage = document.select(PRODUCT_IMAGE_TAG).select("img").attr("src");
        productDTO.setPictureUrl(productImage);
        productDTO.setOldPrice(oldPrice);
        productDTO.setNewPrice(newPrice);

        Double newPriceDouble = Double.parseDouble(newPrice.replace(",", "."));
        Double oldPriceDouble = Double.parseDouble(oldPrice.replace(",", "."));

        if (newPriceDouble > oldPriceDouble) {
            productDTO.setOldPrice(String.format("%.2f", newPriceDouble) + " zł");
            productDTO.setNewPrice(String.format("%.2f", oldPriceDouble) + " zł");
        }
        productDTO.setAmount(AMOUNT_VALUE);
        return productDTO;
    }
}
