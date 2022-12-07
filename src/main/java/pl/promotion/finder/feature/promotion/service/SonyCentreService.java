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
public class SonyCentreService implements Promotion {
    private static final String HOT_SHOT_TAG = "div.prezentation_1";
    private static final String OLD_PRICE_TAG = "type_1";
    private static final String NEW_PRICE_TAG = "type_2";
    private static final String PRODUCT_NAME_TAG = "div.text";
    private static final String SHOP_NAME = "sony centre";
    private static final String PRODUCT_URL = "https://scentre.pl/";

    @Override
    public ProductDTO getPromotion() {
        try {
            Document document = Jsoup.connect(PRODUCT_URL).get();
            return getProduct(document);
        } catch (NullPointerException | ParseException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        Elements elements = document.select(HOT_SHOT_TAG);
        Element productDetails = elements.select("div.desc").last();

        String productUrl = PRODUCT_URL + productDetails.select(A).attr(HREF);
        Elements oldPriceElements = productDetails.getElementsByClass(OLD_PRICE_TAG);
        Element oldPriceElement = oldPriceElements.select(SPAN).first();
        String oldPrice = "";
        if (oldPriceElement.attr("itemprop").equals("price")) {
            oldPrice = oldPriceElement.text();
        }

        Elements newPriceElements = productDetails.getElementsByClass(NEW_PRICE_TAG);
        Element newPriceElement = newPriceElements.select(SPAN).first();
        String newPrice = "";
        if (oldPriceElement.attr("itemprop").equals("price")) {
            newPrice = newPriceElement.text();
        }

        String productName = productDetails.select(PRODUCT_NAME_TAG).text();
        String pictureUrl = PRODUCT_URL + productDetails.select("div.img").select(IMG).attr(SRC);

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(pictureUrl)
                .withProductName(productName)
                .withPictureUrl(productUrl)
                .withOldPrice(oldPrice)
                .withNewPrice(newPrice)
                .build();
    }
}
