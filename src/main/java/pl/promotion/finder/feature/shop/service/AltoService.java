package pl.promotion.finder.feature.shop.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Service
public class AltoService {
    public ProductDTO getAlto() throws IOException {
        URLConnection connection = new URL("https://www.al.to/").openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder altoSB = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            altoSB.append(inputLine);
        }
        Document document = Jsoup.parse(altoSB.toString());
        return getAltoProduct(document);
    }

    private ProductDTO getAltoProduct(Document document) {
        Element element = document.select("div#hotShot").first();
        String amount = element.getElementsByClass("pull-left").first().text().replaceAll("[^\\d]", "");
        String newPrice = element.getElementsByClass("new-price").text();
        String oldPrice = element.getElementsByClass("old-price").text();
        String productName = element.getElementsByClass("product-name").text();
        String productUrl = element.getElementsByClass("img-responsive center-block").attr("src");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(4);
        productDTO.setAmount(amount);
        productDTO.setNewPrice(newPrice);
        productDTO.setOldPrice(oldPrice);
        productDTO.setProductName(productName);
        productDTO.setPictureUrl(productUrl);
        productDTO.setShopName("al.to");
        productDTO.setProductUrl("https://www.al.to/goracy_strzal/19860");
        return productDTO;
    }


}
