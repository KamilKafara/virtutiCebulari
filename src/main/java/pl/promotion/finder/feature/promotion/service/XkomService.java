package pl.promotion.finder.feature.promotion.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
public class XkomService implements Promotion {

    private static final String HOT_SHOT_TAG = "div.sc-bwzfXH.sc-1bb6kqq-2.cNKcdN.sc-htpNat.gSgMmi";
    private static final String PROPERTY_TAG = "sc-1tblmgq-1 grqydx";
    private static final String OLD_PRICE_TAG = "sc-1bb6kqq-4";
    private static final String NEW_PRICE_TAG = "sc-1bb6kqq-5";

    private static final String PRODUCT_NAME_TAG = "sc-1bb6kqq-10 kBnBfM m80syu-0 hGKlIY";
    private static final String AMOUNT_VALUE = "empty";
    private static final String PRODUCT_IMAGE_TAG = "src";
    private static final String SHOP_NAME = "x-kom";
    private static final String SHOP_URL = "https://www.x-kom.pl/";
    private static final String PRODUCT_URL = "https://www.x-kom.pl/goracy_strzal";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(SHOP_URL).get();
            return getProduct(document);
        } catch (NullPointerException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    public ProductDTO getProduct(Document document) {
        Element element = document.select(HOT_SHOT_TAG).first();
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, PRODUCT_URL);
        Elements productProperty = element.getElementsByClass(PROPERTY_TAG);
        productDTO.setPictureUrl(productProperty.attr(PRODUCT_IMAGE_TAG));
        productDTO.setProductName(element.getElementsByClass(PRODUCT_NAME_TAG).text());
        String oldPrice = element.getElementsByClass(OLD_PRICE_TAG).text().replaceAll("zł", "").replaceAll("\\s+", "");
        String newPrice = element.getElementsByClass(NEW_PRICE_TAG).text().replaceAll("zł", "").replaceAll("\\s+", "");

        productDTO.setOldPrice(oldPrice);
        productDTO.setNewPrice(newPrice);

        Double newPriceDouble = Double.parseDouble(newPrice.replaceAll(",", "."));
        Double oldPriceDouble = Double.parseDouble(oldPrice.replaceAll(",", "."));
        if (newPriceDouble > oldPriceDouble) {
            productDTO.setOldPrice(String.format("%.2f", newPriceDouble) + " zł");
            productDTO.setNewPrice(String.format("%.2f", oldPriceDouble) + " zł");
        }
        productDTO.setAmount(AMOUNT_VALUE);
        return productDTO;
    }

}
