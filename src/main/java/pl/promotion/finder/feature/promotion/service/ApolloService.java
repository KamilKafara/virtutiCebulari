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

import static pl.promotion.finder.utils.HtmlTag.*;

@Log4j2
@Service
public class ApolloService implements Promotion {
    private static final String HOT_SHOT_TAG = "daily_offer";
    private static final String NEW_PRICE_TAG = "span.actual";
    private static final String SPAN_OLD = "span.old";
    private static final String PRODUCT_NAME_TAG = "div.img__wrapper";
    private static final String SHOP_NAME = "apollo";
    private static final String PRODUCT_URL = "https://www.apollo.pl/";

    @Override
    public ProductDTO getPromotion() throws IOException, ParseException {
        Document document = HtmlParser.parse(PRODUCT_URL, SHOP_NAME);
        return getProduct(Objects.requireNonNull(document));
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        Elements elements = document.getElementsByClass(HOT_SHOT_TAG);

        String productName = elements.select(PRODUCT_NAME_TAG).select(IMG).attr(ALT);
        String pictureUrl = PRODUCT_URL + elements.select(PRODUCT_NAME_TAG).select(IMG).attr(SRC);
        String oldPrice = elements.select(SPAN_OLD).text();
        String newPrice = elements.select(NEW_PRICE_TAG).text();

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
