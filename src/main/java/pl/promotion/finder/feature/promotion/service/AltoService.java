package pl.promotion.finder.feature.promotion.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.product.dto.ProductDTO;

import java.io.IOException;

@Log4j2
@Service
public class AltoService implements Promotion {

    private static final String NEW_PRICE_TAG = "span.iWkRRi";
    private static final String OLD_PRICE_TAG = "span.jgxIHJ";
    private static final String PRODUCT_NAME_TAG = "span.hGKlIY";
    private static final String PRODUCT_IMAGE_TAG = "span.grqydx";
    private static final String SHOP_NAME = "al.to";
    private static final String PRODUCT_URL = "https://www.al.to/";

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

    @Override
    public ProductDTO getProduct(Document document) {
        Element element = document.select("div.mbxiax-6.CqKkO").first();
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, PRODUCT_URL);
        String productName = element.getElementsByClass(PRODUCT_NAME_TAG).text();
        productDTO.setProductName(productName);
        String newPrice = element.getElementsByClass(NEW_PRICE_TAG).text();
        productDTO.setNewPrice(newPrice);
        String oldPrice = element.getElementsByClass(OLD_PRICE_TAG).text();
        productDTO.setOldPrice(oldPrice);
        String productUrl = element.getElementsByClass(PRODUCT_IMAGE_TAG).attr("src");
        productDTO.setPictureUrl(productUrl);
        productDTO.setAmount("empty");

        return productDTO;
    }


}
