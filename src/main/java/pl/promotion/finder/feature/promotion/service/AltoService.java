package pl.promotion.finder.feature.promotion.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
public class AltoService implements Promotion {
    private static final String HOT_SHOT_TAG = "div.mbxiax-6.CqKkO";
    private static final String NEW_PRICE_TAG = "span.iWkRRi";
    private static final String OLD_PRICE_TAG = "span.jgxIHJ";
    private static final String PRODUCT_NAME_TAG = "span.hGKlIY";
    private static final String PRODUCT_IMAGE_TAG = "span.grqydx";
    private static final String SHOP_NAME = "al.to";
    private static final String PRODUCT_URL = "https://www.al.to/";

    @Override
    public ProductDTO getPromotion() throws IOException, ParseException {
        Document document = HtmlParser.parse(PRODUCT_URL, SHOP_NAME);
        return getProduct(Objects.requireNonNull(document));
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        Element element = document.select(HOT_SHOT_TAG).first();
        String productName = element.getElementsByClass(PRODUCT_NAME_TAG).text();
        String newPrice = element.getElementsByClass(NEW_PRICE_TAG).text();
        String oldPrice = element.getElementsByClass(OLD_PRICE_TAG).text();
        String pictureUrl = element.getElementsByClass(PRODUCT_IMAGE_TAG).attr(SRC);

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(PRODUCT_URL)
                .withProductName(productName)
                .withPictureUrl(pictureUrl)
                .withOldPrice(oldPrice)
                .withNewPrice(newPrice)
                .build();
    }
}
