package pl.promotion.finder.feature.shop.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.IOException;

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
        } catch (NullPointerException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) {
        Elements elements = document.select(HOT_SHOT_TAG);
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, PRODUCT_URL);
        String newPrice = elements.select(NEW_PRICE_TAG).text();
        productDTO.setOldPrice(newPrice);
        productDTO.setNewPrice(newPrice);
        productDTO.setAmount("empty");
        Elements productDetails = elements.select(PRODUCT_DETAILS_TAG).select("img");
        productDTO.setProductName(productDetails.attr("alt"));
        productDTO.setPictureUrl(SHOP_URL + productDetails.attr("src"));
        return productDTO;
    }
}
