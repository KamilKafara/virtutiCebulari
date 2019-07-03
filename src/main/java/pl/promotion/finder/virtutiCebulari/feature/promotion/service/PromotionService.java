package pl.promotion.finder.virtutiCebulari.feature.promotion.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.promotion.finder.virtutiCebulari.feature.promotion.dto.ProductDTO.ProductDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class PromotionService {

    public ProductDTO parseHtml(String shopUrl) throws IOException {
        URL url = new URL("https://www.x-kom.pl/");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        Document document = Jsoup.parse(sb.toString());
        switch (shopUrl) {
            case "x-kom.pl": {
                return getXkomProduct(document);
            }
            default: {
                return null;
            }
        }
    }

    public ProductDTO getXkomProduct(Document document) {
        Elements pElements = document.select("div");
        ProductDTO productDTO = new ProductDTO();
        for (int i = 0; i < pElements.size(); i++) {
            Element element = pElements.get(i);
            if (element.hasClass("hot-shot")) {
                productDTO.setProductName(element.getElementsByClass("product-name").text());
                Elements urlBlock = element.getElementsByClass("img-responsive center-block");
                String productUrl = urlBlock.attr("src");
                productDTO.setPictureUrl(productUrl);

                Double oldPrice = Double.valueOf(element.getElementsByClass("old-price").text().replaceAll(",", ".").replaceAll("[^\\d.]", ""));
                Double newPrice = Double.valueOf(element.getElementsByClass("new-price").text().replaceAll(",", ".").replaceAll("[^\\d.]", ""));
                productDTO.setOldPrice(oldPrice);
                productDTO.setNewPrice(newPrice);

                Elements itemToSold = element.getElementsByClass("pull-left").select("strong");
                if (!itemToSold.isEmpty()) {
                    productDTO.setAmount(Integer.parseInt(itemToSold.text()));
                }
                productDTO.setShopName("x-kom.pl");
                productDTO.setProductUrl("https://www.x-kom.pl/goracy_strzal/");
            }
        }
        return productDTO;
    }

}
