package pl.promotion.finder.feature.promotion.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.dto.ProductDTOBuilder;
import pl.promotion.finder.utils.HtmlParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

import static pl.promotion.finder.utils.HtmlTag.SRC;

@Log4j2
@Service
public class MoreleService implements Promotion {
    private static final String HOT_SHOT_TAG = "div.prom-box-content";
    private static final String OLD_PRICE_TAG = "div.promo-box-old-price";
    private static final String NEW_PRICE_TAG = "div.promo-box-new-price";
    private static final String AMOUNT_TAG = "div.status-box-was";
    private static final String PRODUCT_DETAILS_TAG = "a.prom-box-image";
    private static final String PRODUCT_IMAGE_TAG = "img";
    private static final String SHOP_NAME = "morele.net";
    private static final String PRODUCT_URL = "https://www.morele.net/";
    private static final String PRODUCT_URL_ATTRIBUTE = "href";
    private static final String PRODUCT_NAME_ATTRIBUTE = "title";

    @Override
    public ProductDTO getPromotion() throws IOException, ParseException {
        Document document = HtmlParser.parse(PRODUCT_URL, SHOP_NAME);
        return getProduct(Objects.requireNonNull(document));
    }

    public ProductDTO getProduct(Document document) throws ParseException {
        Elements elements = document.select(HOT_SHOT_TAG);
        Elements productDetails = elements.select(PRODUCT_DETAILS_TAG);
        String productUrl = productDetails.attr(PRODUCT_URL_ATTRIBUTE);
        String productName = productDetails.attr(PRODUCT_NAME_ATTRIBUTE);
        String pictureUrl = productDetails.select(PRODUCT_IMAGE_TAG).first().attr(SRC);
        String oldPrice = elements.select(OLD_PRICE_TAG).text();
        String newPrice = elements.select(NEW_PRICE_TAG).text();
        String amount = elements.select(AMOUNT_TAG).text().replaceAll("\\D+", "");

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(productUrl)
                .withProductName(productName)
                .withPictureUrl(pictureUrl)
                .withOldPrice(oldPrice)
                .withNewPrice(newPrice)
                .withAmount(amount)
                .build();
    }
}
