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
public class ApolloService implements Promotion {
    private static final String hotShotTag = "daily_offer";
    private static final String amountTag = "div.progress__wrapper";
    private static final String newPriceTag = "span.actual";
    private static final String oldPriceTag = "span.old";
    private static final String productNameTag = "name";
    private static final String productImageTag = "img.img-responsive";
    private static final String shopName = "apollo";
    private static final String productURL = "https://www.apollo.pl/";

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
        Elements elements = document.getElementsByClass(hotShotTag);
        ProductDTO productDTO = new ProductDTO(shopName, productURL);

        String oldPrice = elements.select(oldPriceTag).text();
        productDTO.setOldPrice(oldPrice);

        String newPrice = elements.select(newPriceTag).text();
        productDTO.setNewPrice(newPrice);

        String productName = elements.select("div.img__wrapper").select("img").attr("alt");
        productDTO.setProductName(productName);

        String imageUrl = productURL + elements.select("div.img__wrapper").select("img").attr("src");
        productDTO.setPictureUrl(imageUrl);

        return productDTO;
    }
}
