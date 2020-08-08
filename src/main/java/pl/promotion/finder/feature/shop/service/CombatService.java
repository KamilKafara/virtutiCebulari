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
public class CombatService implements Promotion {
    private static final String hotShotTag = "div.product-essential";
    private static final String priceTag = "span.price";
    private static final String productNameTag = "span.base";
    private static final String productImageTag = "div.col-md-10";
    private static final String amountValue = "empty";
    private static final String shopName = "combat";
    private static final String productURL = "https://www.combat.pl/goracy-strzal";

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

    public ProductDTO getProduct(Document document) {
        Elements elements = document.select(hotShotTag);
        ProductDTO productDTO = new ProductDTO(shopName, productURL);
        productDTO.setProductName(elements.select(productNameTag).text());
        String oldPrice = elements.select(priceTag).first().text().replace("zł", "").replace("\\s+", "");
        String newPrice = elements.select(priceTag).last().text().replace("zł", "").replace("\\s+", "");

        String productImage = document.select(productImageTag).select("img").attr("src");
        productDTO.setPictureUrl(productImage);
        productDTO.setOldPrice(oldPrice);
        productDTO.setNewPrice(newPrice);

        Double newPriceDouble = Double.parseDouble(newPrice.replace(",", "."));
        Double oldPriceDouble = Double.parseDouble(oldPrice.replace(",", "."));

        if (newPriceDouble > oldPriceDouble) {
            productDTO.setOldPrice(String.format("%.2f", newPriceDouble) + " zł");
            productDTO.setNewPrice(String.format("%.2f", oldPriceDouble) + " zł");
        }
        productDTO.setAmount(amountValue);
        return productDTO;
    }
}
