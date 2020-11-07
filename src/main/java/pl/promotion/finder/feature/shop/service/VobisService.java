package pl.promotion.finder.feature.shop.service;

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
import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@Log4j2
public class VobisService implements Promotion {
    private static final String SHOP_URL = "https://vobis.pl";
    private static final String PRODUCT_DETAILS_TAG = "a.js-product-name";
    private static final String PRODUCT_NAME_ATTRIBUTE = "data-offer-name";
    private static final String HOT_SHOT_TAG = "div.m-offerBox_inner";
    private static final String OLD_PRICE_TAG = "span.is-old.js-gridOldPrice";
    private static final String NEW_PRICE_TAG = "span.is-new.js-gridPrice";
    private static final String PRODUCT_IMG_TAG = "a.g-gridImg";
    private static final String SHOP_NAME = "vobis";
    private static final String SHOP_PROMOTION_URL = "https://vobis.pl/nocne-wyprzedaze";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(SHOP_PROMOTION_URL).get();
            return getProduct(document);
        } catch (NullPointerException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) {
        Elements elements = document.select(HOT_SHOT_TAG);
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, SHOP_PROMOTION_URL);
        LocalDate date = LocalDate.now();

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Element element = elements.get(dayOfWeek.getValue());

        String oldPrice = element.select(OLD_PRICE_TAG).text();
        productDTO.setOldPrice(oldPrice);

        String newPrice = element.select(NEW_PRICE_TAG).text();
        productDTO.setNewPrice(newPrice);

        Elements photoDetails = element.select(PRODUCT_IMG_TAG);
        Elements productImg = photoDetails.select("img");
        productDTO.setPictureUrl(SHOP_URL + productImg.attr("src"));

        Elements productDetails = element.select("p.m-offerBox_name");
        Elements innerDetail = productDetails.select(PRODUCT_DETAILS_TAG);

        String name = innerDetail.attr(PRODUCT_NAME_ATTRIBUTE);
        productDTO.setProductName(name);

        String productUrl = innerDetail.attr("href");
        productDTO.setProductUrl(SHOP_URL + productUrl);

        return productDTO;
    }
}
