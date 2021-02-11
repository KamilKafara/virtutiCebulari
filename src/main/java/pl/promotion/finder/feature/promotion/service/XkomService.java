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

import java.io.IOException;

@Log4j2
@Service
public class XkomService implements Promotion {

    private static final String HOT_SHOT_TAG = "div.iRfMJZ";
    private static final String PROPERTY_TAG = "div.hyIQof";
    private static final String OLD_PRICE_TAG = "span.dNOsIM";
    private static final String NEW_PRICE_TAG = "span.bxyEHo";

    private static final String AMOUNT_VALUE = "empty";
    private static final String SHOP_NAME = "x-kom";
    private static final String SHOP_URL = "https://www.x-kom.pl/";
    private static final String PRODUCT_URL = "https://www.x-kom.pl/goracy_strzal";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(SHOP_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/70.0")
                    .followRedirects(true)
                    .timeout(100)
                    .ignoreContentType(true)
                    .get();

            return getProduct(document);
        } catch (NullPointerException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    public ProductDTO getProduct(Document document) {

        Elements elements = document.select(HOT_SHOT_TAG);
        if (elements.size() != 2) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, PRODUCT_URL);

        Element productInfo = elements.first();
        Elements productProperty = productInfo.select("img.hyIQof");

        String productImage = productProperty.attr("src");
        String productName = productProperty.attr("alt");

        productDTO.setPictureUrl(productImage);
        productDTO.setProductName(productName);

        productDTO.setAmount(AMOUNT_VALUE);

        String oldPrice = elements.select(OLD_PRICE_TAG).text().replaceAll("zł", "").replaceAll("\\s+", "");
        String newPrice = elements.select(NEW_PRICE_TAG).text().replaceAll("zł", "").replaceAll("\\s+", "");

        productDTO.setOldPrice(oldPrice);
        productDTO.setNewPrice(newPrice);

        Double newPriceDouble = Double.parseDouble(newPrice.replaceAll(",", "."));
        Double oldPriceDouble = Double.parseDouble(oldPrice.replaceAll(",", "."));
        if (newPriceDouble > oldPriceDouble) {
            productDTO.setOldPrice(String.format("%.2f", newPriceDouble) + " zł");
            productDTO.setNewPrice(String.format("%.2f", oldPriceDouble) + " zł");
        }
        return productDTO;
    }

}
