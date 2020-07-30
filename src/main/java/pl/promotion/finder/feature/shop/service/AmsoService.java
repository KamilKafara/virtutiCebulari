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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Log4j2
@Service
public class AmsoService implements Promotion {
    private static final String shopName = "al.to";
    private static final String productURL = "https://amso.pl/";
    private static final String shopURL = "https://amso.pl/";
    private static final String hotShotTag = "main_hotspot_zone1";
    private static final String amountTag = "pts_total";
    private static final String newPriceTag = "price";
    private static final String oldPriceTag = "max-price";
    private static final String productNameTag = "product-name";
    private static final String productImageTag = "product-icon";

    @Override
    public ProductDTO getPromotion() {
        try {
            URL urlAmso = new URL(shopURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlAmso.openStream()));
            String inputLine;
            StringBuilder amsoSB = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                amsoSB.append(inputLine);
            }
            Document amsoDocument = Jsoup.parse(amsoSB.toString());
            return getAmsoProduct(amsoDocument);
        } catch (NullPointerException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    private ProductDTO getAmsoProduct(Document document) {
        Element elements = document.getElementById(hotShotTag);
        ProductDTO productDTO = new ProductDTO(shopName, productURL);
        String amount = elements.getElementById(amountTag).text();
        String newPrice = elements.getElementsByClass(newPriceTag).text();
        String oldPrice = elements.getElementsByClass(oldPriceTag).text();
        String productName = elements.getElementsByClass(productNameTag).attr("title");
        Elements imageElement = elements.getElementsByClass(productImageTag);
        Elements imageUrl = imageElement.select("img");
        for (Element element : imageUrl) {
            productDTO.setPictureUrl(shopURL + element.select("img").attr("data-src"));
            break;
        }
        productDTO.setProductName(productName);
        productDTO.setOldPrice(oldPrice);
        productDTO.setNewPrice(newPrice);
        productDTO.setAmount(amount.replaceAll("[^\\d]", ""));

        return productDTO;
    }


}
