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
import pl.promotion.finder.feature.product.dto.PriceMapper;
import pl.promotion.finder.feature.product.dto.ProductDTO;

import java.io.IOException;
import java.text.ParseException;

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
    public ProductDTO getPromotion() {
        try {
            Document document = Jsoup.connect(PRODUCT_URL).get();
            return getProduct(document);
        } catch (NullPointerException | IOException | ParseException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        Element elements = document.getElementById(HOT_SHOT_TAG);
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, PRODUCT_URL);

        String newPrice = elements.getElementsByClass(NEW_PRICE_TAG).text();
        productDTO.setNewPrice(PriceMapper.priceFactory(newPrice));
        String oldPrice = elements.getElementsByClass(OLD_PRICE_TAG).text();
        productDTO.setOldPrice(PriceMapper.priceFactory(oldPrice));
        String productName = elements.getElementsByClass(PRODUCT_NAME_TAG).attr("title");
        productDTO.setProductName(productName);
        Elements imageElement = elements.getElementsByClass(PRODUCT_IMAGE_TAG);
        productDTO.setPictureUrl(PRODUCT_URL + imageElement.select("img").attr("data-src"));
        String amount = elements.getElementById(AMOUNT_TAG).text();
        productDTO.setAmount(amount.replaceAll("[^\\d]", ""));

        return productDTO;
    }


}
