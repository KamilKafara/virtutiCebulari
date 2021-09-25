package pl.promotion.finder.feature.promotion.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.dto.ProductDTOBuilder;
import pl.promotion.finder.utils.HtmlParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

import static pl.promotion.finder.utils.HtmlTag.DATA_SRC;
import static pl.promotion.finder.utils.HtmlTag.IMG;

@Log4j2
@Service
public class AmsoService implements Promotion {
    private static final String SHOP_NAME = "amso";
    private static final String PRODUCT_URL = "https://amso.pl/";
    private static final String HOT_SHOT_TAG = "main_hotspot_zone1";
    private static final String AMOUNT_TAG = "pts_total";
    private static final String NEW_PRICE_TAG = "price";
    private static final String OLD_PRICE_TAG = "max-price";
    private static final String PRODUCT_NAME_TAG = "product-name";
    private static final String PRODUCT_IMAGE_TAG = "product-icon";

    @Override
    public ProductDTO getPromotion() throws IOException, ParseException {
        Document document = HtmlParser.parse(PRODUCT_URL, SHOP_NAME);
        return getProduct(Objects.requireNonNull(document));
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        Element elements = document.getElementById(HOT_SHOT_TAG);

        String newPrice = elements.getElementsByClass(NEW_PRICE_TAG).text();
        String oldPrice = elements.getElementsByClass(OLD_PRICE_TAG).text();
        String productName = elements.getElementsByClass(PRODUCT_NAME_TAG).attr("title");
        Elements imageElement = elements.getElementsByClass(PRODUCT_IMAGE_TAG);
        String pictureUrl = PRODUCT_URL + imageElement.select(IMG).attr(DATA_SRC);
        Element amountElement = elements.getElementById(AMOUNT_TAG);
        String amount = amountElement.text().replaceAll("[^\\d]", "");

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(PRODUCT_URL)
                .withProductName(productName)
                .withPictureUrl(pictureUrl)
                .withOldPrice(oldPrice)
                .withNewPrice(newPrice)
                .withAmount(amount)
                .build();
    }
}
