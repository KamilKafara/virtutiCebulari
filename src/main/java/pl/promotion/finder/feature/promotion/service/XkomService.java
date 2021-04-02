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
import java.math.BigDecimal;
import java.text.ParseException;

@Log4j2
@Service
public class XkomService implements Promotion {

    private static final String HOT_SHOT_TAG = "div.kNOaST";
    private static final String PROPERTY_TAG = "span.sc-1tblmgq-0.sc-18w91q-5.lcCghT.sc-1tblmgq-2.bmAqLj";
    private static final String OLD_PRICE_TAG = "span.dNOsIM";
    private static final String NEW_PRICE_TAG = "span.bxyEHo";

    private static final String SHOP_NAME = "x-kom";
    private static final String SHOP_URL = "https://www.x-kom.pl/";
    private static final String PRODUCT_URL = "https://www.x-kom.pl/goracy_strzal";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(SHOP_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/70.0")
                    .followRedirects(true)
                    .timeout(1000)
                    .ignoreContentType(true)
                    .get();

            return getProduct(document);
        } catch (NullPointerException | ParseException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    public ProductDTO getProduct(Document document) throws ParseException {
        Elements elements = document.select(HOT_SHOT_TAG);
        if (elements.isEmpty()) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO(SHOP_NAME, PRODUCT_URL);

        Element productInfo = elements.first();
        Element productProperty = productInfo.select(PROPERTY_TAG).first();

        String productImage = productProperty.select("img").attr("src");
        String productName = productProperty.select("img").attr("alt");

        productDTO.setPictureUrl(productImage);
        productDTO.setProductName(productName);

        String oldPriceText = elements.select(OLD_PRICE_TAG).text();
        String newPriceText = elements.select(NEW_PRICE_TAG).text();

        String newPrice = PriceMapper.priceFactory(oldPriceText);
        productDTO.setNewPrice(newPrice);
        BigDecimal bigDecimalNewPrice = PriceMapper.getDecimalPrice();

        String oldPrice = PriceMapper.priceFactory(newPriceText);
        productDTO.setOldPrice(oldPrice);
        BigDecimal bigDecimalOldPrice = PriceMapper.getDecimalPrice();

        if (bigDecimalNewPrice.doubleValue() > bigDecimalOldPrice.doubleValue()) {
            productDTO.setOldPrice(newPrice);
            productDTO.setNewPrice(oldPrice);
        }
        return productDTO;
    }
}
