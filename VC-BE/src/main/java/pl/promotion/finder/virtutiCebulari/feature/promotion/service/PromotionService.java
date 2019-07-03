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
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService {

    public List<ProductDTO> getAllPromotion() throws IOException {
        List<ProductDTO> productDTOList = new ArrayList<>();
        URLConnection connection = new URL("https://www.morele.net/").openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

        StringBuilder moreleSB = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            moreleSB.append(line);
        }

        Document moreleDocument = Jsoup.parse(moreleSB.toString());
        ProductDTO moreleProduct = getMoreleProduct(moreleDocument);

        URL urlXkom = new URL("https://www.x-kom.pl/");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlXkom.openStream()));
        String inputLine;
        StringBuilder xKomSB = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            xKomSB.append(inputLine);
        }
        Document xKomDocument = Jsoup.parse(xKomSB.toString());
        ProductDTO xkomProduct = getXkomProduct(xKomDocument);
        productDTOList.add(xkomProduct);
        productDTOList.add(moreleProduct);
        return productDTOList;
    }


    private ProductDTO getXkomProduct(Document document) {
        Elements pElements = document.select("div");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
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

    public ProductDTO getPromotionByShop(String shopName) {
        return null;
    }
}
