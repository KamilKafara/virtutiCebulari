package pl.promotion.finder.feature.shop.service;

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

@Service
public class AmsoService {
    public ProductDTO getAmso() throws IOException {
        try {
            URL urlAmso = new URL("https://amso.pl/");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlAmso.openStream()));
            String inputLine;
            StringBuilder amsoSB = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                amsoSB.append(inputLine);
            }
            Document amsoDocument = Jsoup.parse(amsoSB.toString());
            return getAmsoProduct(amsoDocument);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in amso.", new FieldInfo("amso", ErrorCode.NOT_FOUND));
        }

    }

    private ProductDTO getAmsoProduct(Document document) {
        Element elements = document.getElementById("main_hotspot_zone1");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3);
        productDTO.setShopName("https://amso.pl");
        String amount = elements.getElementById("pts_total").text();
        String price = elements.getElementsByClass("price").text();
        String oldPrice = elements.getElementsByClass("max-price").text();
        String productName = elements.getElementsByClass("product-name").attr("title");
        String productUrl = "https://amso.pl" + elements.getElementsByClass("product-name").attr("href");

        Elements imageElement = elements.getElementsByClass("product-icon");
        Elements imageUrl = imageElement.select("img");
        for (Element element : imageUrl) {
            productDTO.setPictureUrl("https://amso.pl" + element.select("img").attr("data-src"));
            break;
        }
        productDTO.setProductName(productName);
        productDTO.setOldPrice(oldPrice);
        productDTO.setNewPrice(price);
        productDTO.setAmount(amount.replaceAll("[^\\d]", ""));
        productDTO.setProductUrl(productUrl);

        return productDTO;
    }

}
