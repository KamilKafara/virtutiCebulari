package pl.promotion.finder.virtutiCebulari.feature.morele;

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
import java.net.URLConnection;
import java.nio.charset.Charset;

@Service
public class MoreleService {

    public ProductDTO getMorele() throws IOException {
        StringBuilder moreleSB = bufferMoreleURL("https://www.morele.net/");
        Document moreleDocument = Jsoup.parse(moreleSB.toString());
        return getMoreleProduct(moreleDocument);
    }

    private StringBuilder bufferMoreleURL(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
        StringBuilder moreleSB = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            moreleSB.append(line);
        }
        return moreleSB;
    }

    private ProductDTO getMoreleProduct(Document document) {
        Elements elements = document.select("div");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(2L);
        for (Element element : elements) {
            if (element.hasClass("prom-box-content")) {
                Elements oldPrice = element.getElementsByClass("promo-box-old-price");
                Elements newPrice = element.getElementsByClass("promo-box-new-price");
                productDTO.setOldPrice(oldPrice.text());
                productDTO.setNewPrice(newPrice.text());
                Elements productNameElements = element.getElementsByClass("promo-box-name").select("a");
                String productName = productNameElements.attr("title");
                String productUrl = productNameElements.attr("href");
                productDTO.setProductName(productName);
                productDTO.setProductUrl(productUrl);
                productDTO.setShopName("morene.net");
            }
            if (element.hasClass("promo-box-availability")) { // liczba sztuk
                Elements availabilityBlock = element.getElementsByClass("promo-box-availability");
                for (Element availabilityElement : availabilityBlock) {
                    String soldItem = availabilityElement.getElementsByClass("status-box-expired").text().replaceAll("Sprzedano ", "");
                    String remainedItem = availabilityElement.getElementsByClass("status-box-was").text().replaceAll("Pozostało ", "");
                    productDTO.setAmount(remainedItem);
                }
            }
            String pictureUrl = element.getElementsByClass("prom-box-image").select("img").attr("src");
            if (pictureUrl.length() > 3) {
                productDTO.setPictureUrl(element.getElementsByClass("prom-box-image").select("img").attr("src"));
            }
        }

        return productDTO;
    }

}
