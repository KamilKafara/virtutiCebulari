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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Log4j2
@Service
public class AltoService implements Promotion {

    private static final String hotShotTag = "div#hotShot";
    private static final String amountTag = "pull-left";
    private static final String newPriceTag = "new-price";
    private static final String oldPriceTag = "old-price";
    private static final String productNameTag = "product-name";
    private static final String productImageTag = "img-responsive center-block";
    private static final String shopName = "al.to";
    private static final String productURL = "https://www.al.to/goracy_strzal/";
    private static final String connectionProperty = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            URLConnection connection = new URL(productURL).openConnection();
            connection.setRequestProperty("User-Agent", connectionProperty);
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder altoSB = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                altoSB.append(inputLine);
            }
            Document document = Jsoup.parse(altoSB.toString());
            return getAltoProduct(document);
        } catch (NullPointerException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    private ProductDTO getAltoProduct(Document document) {
        Element element = document.select(hotShotTag).first();
        ProductDTO productDTO = new ProductDTO(shopName, productURL);
        String productName = element.getElementsByClass(productNameTag).text();
        productDTO.setProductName(productName);
        String newPrice = element.getElementsByClass(newPriceTag).text();
        productDTO.setNewPrice(newPrice);
        String oldPrice = element.getElementsByClass(oldPriceTag).text();
        productDTO.setOldPrice(oldPrice);
        String productUrl = element.getElementsByClass(productImageTag).attr("src");
        productDTO.setPictureUrl(productUrl);
        String amount = element.getElementsByClass(amountTag).first().text().replaceAll("[^\\d]", "");
        productDTO.setAmount(amount);

        return productDTO;
    }


}
