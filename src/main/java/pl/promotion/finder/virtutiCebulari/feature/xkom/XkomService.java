package pl.promotion.finder.virtutiCebulari.feature.xkom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.promotion.finder.virtutiCebulari.feature.dto.ProductDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class XkomService {
    public ProductDTO getXkom() throws IOException {

        URL urlXkom = new URL("https://www.x-kom.pl/");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlXkom.openStream()));
        String inputLine;
        StringBuilder xkomSB = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            xkomSB.append(inputLine);
        }
//        StringBuilder xkomSB = bufferURL("https://www.x-kom.pl/");
        Document xKomDocument = Jsoup.parse(xkomSB.toString());
        return getXkomProduct(xKomDocument);
    }

    private ProductDTO getXkomProduct(Document document) {
        Elements pElements = document.select("div");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1);
        for (Element element : pElements) {
            if (element.hasClass("hot-shot")) {
                productDTO.setProductName(element.getElementsByClass("product-name").text());
                Elements urlBlock = element.getElementsByClass("img-responsive center-block");
                String productUrl = urlBlock.attr("src");
                productDTO.setPictureUrl(productUrl);

                String oldPrice = element.getElementsByClass("old-price").text();
                String newPrice = element.getElementsByClass("new-price").text();
                productDTO.setOldPrice(oldPrice);
                productDTO.setNewPrice(newPrice);

                Elements soldItem = element.getElementsByClass("pull-left").select("strong");
                if (!soldItem.isEmpty()) {
                    productDTO.setAmount(soldItem.text());
                }
                productDTO.setShopName("x-kom.pl");
                productDTO.setProductUrl("https://www.x-kom.pl/goracy_strzal/");
            }
        }
        return productDTO;
    }

}
