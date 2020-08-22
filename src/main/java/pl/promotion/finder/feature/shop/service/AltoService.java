package pl.promotion.finder.feature.shop.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.IOException;

@Log4j2
@Service
public class AltoService implements Promotion {

    private static final String hotShotTag = "div.mbxiax-1.nnbTK";
    private static final String newPriceTag = "span.iWkRRi";
    private static final String oldPriceTag = "span.jgxIHJ";
    private static final String productNameTag = "span.hGKlIY";
    private static final String productImageTag = "span.grqydx";
    private static final String shopName = "al.to";
    private static final String productURL = "https://www.al.to/";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            Document document = Jsoup.connect(productURL).get();
            return getProduct(document);
        } catch (NullPointerException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) {
        Element element = document.select("div.mbxiax-6.CqKkO").first();
        ProductDTO productDTO = new ProductDTO(shopName, productURL);
        String productName = element.getElementsByClass(productNameTag).text();
        productDTO.setProductName(productName);
        String newPrice = element.getElementsByClass(newPriceTag).text();
        productDTO.setNewPrice(newPrice);
        String oldPrice = element.getElementsByClass(oldPriceTag).text();
        productDTO.setOldPrice(oldPrice);
        String productUrl = element.getElementsByClass(productImageTag).attr("src");
        productDTO.setPictureUrl(productUrl);
        productDTO.setAmount("empty");

        return productDTO;
    }


}
