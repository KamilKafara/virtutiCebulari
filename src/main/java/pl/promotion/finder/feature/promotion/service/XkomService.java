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
import pl.promotion.finder.feature.product.dto.ProductDTOBuilder;

import java.io.IOException;
import java.text.ParseException;

import static pl.promotion.finder.utils.HtmlTag.*;

@Log4j2
@Service
public class XkomService implements Promotion {
    private static final String XKOM_TIMEOUT = System.getenv("XKOM_TIMEOUT");
    private static final String XKOM_AGENT_DETAILS = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/70.0";

    private static final String HOT_SHOT_TAG = "div.dyQMoT";
    private static final String PROPERTY_TAG = "div.sc-1bb6kqq-1";
    private static final String OLD_PRICE_TAG = "span.lfqgAC";
    private static final String NEW_PRICE_TAG = "span.ccdajt";

    private static final String SHOP_NAME = "x-kom";
    private static final String SHOP_URL = "https://www.x-kom.pl/";
    private static final String PRODUCT_URL = SHOP_URL + "goracy_strzal";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(SHOP_URL)
                    .userAgent(XKOM_AGENT_DETAILS)
                    .followRedirects(true)
                    .timeout(Integer.parseInt(XKOM_TIMEOUT))
                    .ignoreContentType(true)
                    .get();

            return getProduct(document);
        } catch (NullPointerException | ParseException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    public ProductDTO getProduct(Document document) throws ParseException {
        Elements elements = document.select(HOT_SHOT_TAG);
        if (elements.isEmpty()) {
            return new ProductDTO();
        }
        Element productInfo = elements.first();
        Element productProperty = productInfo.select(PROPERTY_TAG).first();
        if (productProperty == null) {
            return new ProductDTO();
        }
        String productName = productProperty.select(IMG).attr(ALT);
        String productUrl = productProperty.select(IMG).attr(SRC);
        String oldPriceText = elements.select(OLD_PRICE_TAG).text();
        String newPriceText = elements.select(NEW_PRICE_TAG).text();

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(PRODUCT_URL)
                .withProductName(productName)
                .withPictureUrl(productUrl)
                .withOldPrice(oldPriceText)
                .withNewPrice(newPriceText)
                .build();
    }
}
