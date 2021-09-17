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
import pl.promotion.finder.feature.product.dto.ProductDTOBuilder;

import java.io.IOException;
import java.text.ParseException;

import static pl.promotion.finder.utils.HtmlTag.*;

@Log4j2
@Service
public class ZadowolenieService implements Promotion {
    private static final String SHOP_NAME = "zadowolenie";
    private static final String SHOP_URL = "https://zadowolenie.pl/";
    private static final String PRODUCT_URL = "https://zadowolenie.pl/";
    private static final String HOT_SHOT_TAG = "div.b-dayOffer.product_box_widget";
    private static final String NEW_PRICE_TAG = "span.js-gridPrice";
    private static final String PRODUCT_DETAILS_TAG = "a.g-gridImg.g-productBoxWidget_img.js-gridImg";

    @Override
    public ProductDTO getPromotion() {
        try {
            Document document = Jsoup.connect(SHOP_URL).get();
            return getProduct(document);
        } catch (NullPointerException | IOException | ParseException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        Elements elements = document.select(HOT_SHOT_TAG);
        String newPrice = elements.select(NEW_PRICE_TAG).text();
        Elements productDetails = elements.select(PRODUCT_DETAILS_TAG).select(IMG);
        String productName = productDetails.attr(ALT);
        String pictureUrl = SHOP_URL + productDetails.attr(SRC);

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(PRODUCT_URL)
                .withProductName(productName)
                .withPictureUrl(pictureUrl)
                .withOldPrice(newPrice)
                .withNewPrice(newPrice)
                .build();
    }
}
