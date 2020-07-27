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
public class CarinetService {
    public ProductDTO getCarinet() throws IOException {
        try {
            URL urlCarinet = new URL("https://carinet.pl/pl/");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCarinet.openStream()));
            String inputLine;
            StringBuilder carinetSB = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                carinetSB.append(inputLine);
            }
            Document carinetDocument = Jsoup.parse(carinetSB.toString());
            return getCarinetProduct(carinetDocument);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in carinet.", new FieldInfo("carinet", ErrorCode.NOT_FOUND));
        }

    }

    private ProductDTO getCarinetProduct(Document document) {
        Elements elements = document.getElementsByClass("hotshot");

        String imageUrl = elements.tagName("img").select("img.img-responsive").attr("src");
        String amount = elements.tagName("div").select("div.availability").select("span").text().replaceAll("[^\\d]", "");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(5);
        Elements productUrl = elements.tagName("a").select("a");
        for (Element element : productUrl) {
            productDTO.setProductName(element.getElementsByTag("h4").text());
            String item = element.select("a").first().attr("href");
            productDTO.setProductUrl(item);
        }
        Element oldPrice = document.select("span.old").first();
        Element newPrice = document.select("span.new").first();

        productDTO.setNewPrice(newPrice.text());
        productDTO.setOldPrice(oldPrice.text());
        productDTO.setPictureUrl(imageUrl);
        productDTO.setAmount(amount.replaceAll("[^\\d]", ""));
        productDTO.setShopName("https://carinet.pl");
        return productDTO;
    }
}
