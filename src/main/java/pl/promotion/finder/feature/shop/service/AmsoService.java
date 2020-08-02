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
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.IOException;

@Log4j2
@Service
public class AmsoService implements Promotion {
    private static final String shopName = "amso";
    private static final String productURL = "https://amso.pl/";
    private static final String hotShotTag = "main_hotspot_zone1";
    private static final String amountTag = "pts_total";
    private static final String newPriceTag = "price";
    private static final String oldPriceTag = "max-price";
    private static final String productNameTag = "product-name";
    private static final String productImageTag = "product-icon";

    @Override
    public ProductDTO getPromotion() {
        try {
            Document document = Jsoup.connect(productURL).get();
            return getProduct(document);
        } catch (NullPointerException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) {
        Element elements = document.getElementById(hotShotTag);
        ProductDTO productDTO = new ProductDTO(shopName, productURL);

        String newPrice = elements.getElementsByClass(newPriceTag).text();
        productDTO.setNewPrice(newPrice);
        String oldPrice = elements.getElementsByClass(oldPriceTag).text();
        productDTO.setOldPrice(oldPrice);
        String productName = elements.getElementsByClass(productNameTag).attr("title");
        productDTO.setProductName(productName);
        Elements imageElement = elements.getElementsByClass(productImageTag);
        productDTO.setPictureUrl(productURL + imageElement.select("img").attr("data-src"));
        String amount = elements.getElementById(amountTag).text();
        productDTO.setAmount(amount.replaceAll("[^\\d]", ""));

        return productDTO;
    }


}
