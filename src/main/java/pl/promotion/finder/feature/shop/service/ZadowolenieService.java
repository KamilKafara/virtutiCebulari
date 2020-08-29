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
    private static final String shopName = "zadowolenie";
    private static final String shopURL = "https://zadowolenie.pl/";
    private static final String productURL = "https://zadowolenie.pl/";
    private static final String hotShotTag = "div.b-dayOffer.product_box_widget";
    private static final String newPriceTag = "span.js-gridPrice";
    private static final String productDetailsTag = "a.g-gridImg.g-productBoxWidget_img.js-gridImg";

    @Override
    public ProductDTO getPromotion() {
        try {
            Document document = Jsoup.connect(shopURL).get();
            return getProduct(document);
        } catch (NullPointerException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) {
        Elements elements = document.select(hotShotTag);
        ProductDTO productDTO = new ProductDTO(shopName, productURL);
        String newPrice = elements.select(newPriceTag).text();
        productDTO.setOldPrice(newPrice);
        productDTO.setNewPrice(newPrice);
        productDTO.setAmount("empty");
        Elements productDetails = elements.select(productDetailsTag).select("img");
        productDTO.setProductName(productDetails.attr("alt"));
        productDTO.setPictureUrl(shopURL + productDetails.attr("src"));
        return productDTO;
    }
}
