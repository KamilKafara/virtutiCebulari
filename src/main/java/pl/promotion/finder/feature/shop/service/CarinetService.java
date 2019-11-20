package pl.promotion.finder.feature.shop.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class CarinetService {
    public ProductDTO getCarinet() throws IOException {

        URL urlCarinet = new URL("https://carinet.pl/pl/");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlCarinet.openStream()));
        String inputLine;
        StringBuilder carinetSB = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            carinetSB.append(inputLine);
//            System.out.println(inputLine);
        }
        Document carinetDocument = Jsoup.parse(carinetSB.toString());
        return getCarinetProduct(carinetDocument);
    }

    private ProductDTO getCarinetProduct(Document document) {
        Elements elements = document.getElementsByClass("hotshot");

        String imageUrl = elements.tagName("img").select("img.img-responsive").attr("src");
        String amount = elements.tagName("div").select("div.availability").select("span").text().replaceAll("[^\\d]", "");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(5);
        Elements productUrl = elements.tagName("a").select("a");
        for (Element element : productUrl) {
            if (element.getElementsByTag("a").hasAttr("href")) {
                productDTO.setProductUrl(element.attr("href"));
                break;
            }
        }

        productDTO.setPictureUrl(imageUrl);
        productDTO.setAmount(amount.replaceAll("[^\\d]", ""));
        productDTO.setShopName("https://carinet.pl");
        return productDTO;
    }
}
