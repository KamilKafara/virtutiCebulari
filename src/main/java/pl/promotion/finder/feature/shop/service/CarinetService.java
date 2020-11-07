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

@Log4j2
@Service
public class CarinetService implements Promotion {
    private static final String HOT_SHOT_TAG = "hotshot";
    private static final String AMOUNT_TAG = "div.availability";
    private static final String NEW_PRICE_TAG = "span.old";
    private static final String OLD_PRICE_TAG = "span.new";
    private static final String PRODUCT_NAME_TAG = "h4";
    private static final String PRODUCT_IMAGE_TAG = "img.img-responsive";
    private static final String SHOP_NAME = "carinet";
    private static final String PRODUCT_URL = "https://carinet.pl/pl/";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(PRODUCT_URL).get();
            return getProduct(document);
        } catch (NullPointerException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    public ProductDTO getProduct(Document document) {
        Elements elements = document.getElementsByClass(HOT_SHOT_TAG);
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, PRODUCT_URL);

        String imageUrl = elements.select(PRODUCT_IMAGE_TAG).attr("src");
        productDTO.setPictureUrl(imageUrl);
        productDTO.setProductName(elements.select(PRODUCT_NAME_TAG).text());
        String productUrl = elements.select("a").attr("href");
        productDTO.setProductUrl(productUrl);
        Element oldPrice = document.select(NEW_PRICE_TAG).first();
        productDTO.setOldPrice(oldPrice.text().replace(",",""));
        Element newPrice = document.select(OLD_PRICE_TAG).first();
        productDTO.setNewPrice(newPrice.text().replace(",",""));
        String amount = elements.select(AMOUNT_TAG).select("span").text().replaceAll("[^\\d]", "");
        productDTO.setAmount(amount.replaceAll("[^\\d]", ""));

        return productDTO;
    }
}
