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
import java.net.URLConnection;
import java.nio.charset.Charset;

@Log4j2
@Service
public class MoreleService implements Promotion {
    private static final String hotShotTag = "div#hotShot";
    private static final String amountTag = "pull-left";
    private static final String newPriceTag = "new-price";
    private static final String oldPriceTag = "old-price";
    private static final String productNameTag = "product-name";
    private static final String productImageTag = "img-responsive center-block";
    private static final String shopName = "morene.net";
    private static final String productURL = "https://www.morele.net/";


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
        ProductDTO productDTO = new ProductDTO(shopName, productURL);
        for (Element element : elements) {
            if (element.hasClass("prom-box-content")) {
                Elements oldPrice = element.getElementsByClass("promo-box-old-price");
                Elements newPrice = element.getElementsByClass("promo-box-new-price");
                productDTO.setOldPrice(oldPrice.text());
                productDTO.setNewPrice(newPrice.text());
                Elements productNameElements = element.getElementsByClass("promo-box-name").select("a");
                String productName = productNameElements.attr("title");
                String productUrl = productNameElements.attr("href");
                if (productUrl.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in morele.", new FieldInfo("morele", ErrorCode.NOT_FOUND));
                }
                productDTO.setProductName(productName);
//                productDTO.setProductUrl(productUrl);
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
        if (elements.isEmpty()) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
        }

        return productDTO;
    }

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            StringBuilder moreleSB = bufferMoreleURL(productURL);
            Document moreleDocument = Jsoup.parse(moreleSB.toString());
            return getMoreleProduct(moreleDocument);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in morele.", new FieldInfo("morele", ErrorCode.NOT_FOUND));
        }
    }
}
