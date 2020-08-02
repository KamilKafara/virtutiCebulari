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
public class MoreleService implements Promotion {
    private static final String hotShotTag = "div.prom-box-content";
    private static final String oldPriceTag = "div.promo-box-old-price";
    private static final String newPriceTag = "div.promo-box-new-price";
    private static final String amountTag = "div.status-box-was";
    private static final String productDetailsTag = "a.prom-box-image";
    private static final String productImageTag = "img";
    private static final String shopName = "morele.net";
    private static final String productURL = "https://www.morele.net/";
    private static final String productUrlAttribute = "href";
    private static final String productNameAttribute = "title";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(productURL).get();
            return getProduct(document);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND));
        }
    }

    public ProductDTO getProduct(Document document) {
        Elements elements = document.select(hotShotTag);
        ProductDTO productDTO = new ProductDTO(shopName, "");

        Elements productDetails = elements.select(productDetailsTag);
        productDTO.setProductUrl(productDetails.attr(productUrlAttribute));
        productDTO.setProductName(productDetails.attr(productNameAttribute));
        productDTO.setPictureUrl(productDetails.select(productImageTag).first().attr("src"));
        productDTO.setOldPrice(elements.select(oldPriceTag).text());
        productDTO.setNewPrice(elements.select(newPriceTag).text());
        productDTO.setAmount(elements.select(amountTag).text().replaceAll("\\D+", ""));
        return productDTO;
    }
}
