package pl.promotion.finder.virtutiCebulari.feature.alto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import pl.promotion.finder.virtutiCebulari.feature.dto.ProductDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class AltoService {
    public ProductDTO getAlto() throws IOException {

        URL urlAlto = new URL("https://www.al.to/");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlAlto.openStream()));
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
        System.out.println(element);
        String amount = element.getElementsByClass("pull-left").first().text().replaceAll("[^\\d]", "");
        String newPrice = element.getElementsByClass("new-price").text();
        String oldPrice = element.getElementsByClass("old-price").text();
        String productName = element.getElementsByClass("product-name").text();
        String productUrl = element.getElementsByClass("img-responsive center-block").attr("src");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setAmount(amount);
        productDTO.setNewPrice(newPrice);
        productDTO.setOldPrice(oldPrice);
        productDTO.setProductName(productName);
        productDTO.setPictureUrl(productUrl);
        productDTO.setProductUrl("https://www.al.to/goracy_strzal/19860");
        return productDTO;
    }


}
