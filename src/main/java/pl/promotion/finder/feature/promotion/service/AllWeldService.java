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
public class AllWeldService implements Promotion {
    private static final String HOT_SHOT_TAG = "fx-hit-day-custom";
    private static final String PRODUCT_DETAILS = "div.innerbox";
    private static final String PRICE_TAG = "div.price";
    private static final String PRODUCT_NAME_TAG = "span.productname";
    private static final String SHOP_NAME = "allweld";
    private static final String PRODUCT_URL = "https://allweld.pl/";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(PRODUCT_URL).get();
            return getProduct(document);
        } catch (NullPointerException | ParseException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        Element elements = document.getElementById(HOT_SHOT_TAG);
        if (elements == null) {
            return new ProductDTO();
        }
        Element productDetails = elements.select(PRODUCT_DETAILS).last();
        if (productDetails == null) {
            return new ProductDTO();
        }
        Elements prices = productDetails.select(PRICE_TAG);
        if (prices.isEmpty()) {
            return new ProductDTO();
        }
        String productUrl = productDetails.select(A).attr(HREF);
        String productName = productDetails.select(PRODUCT_NAME_TAG).text();
        String pictureUrl = PRODUCT_URL + productDetails.select("a.row").select(IMG).attr(DATA_SRC);

        String oldPrice = prices.select(EM).text();
        String newPrice = prices.select(DEL).text();

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(productUrl)
                .withProductName(productName)
                .withPictureUrl(pictureUrl)
                .withOldPrice(oldPrice)
                .withNewPrice(newPrice)
                .build();
    }
}
