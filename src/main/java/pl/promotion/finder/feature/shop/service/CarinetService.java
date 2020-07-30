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

@Log4j2
@Service
public class CarinetService implements Promotion {
    private static final String hotShotTag = "hotshot";
    private static final String amountTag = "div.availability";
    private static final String newPriceTag = "span.old";
    private static final String oldPriceTag = "span.new";
    private static final String productNameTag = "h4";
    private static final String productImageTag = "img.img-responsive";
    private static final String shopName = "carinet";
    private static final String productURL = "https://carinet.pl/pl";
    private static final String productUrlTag = "a.href";

    @Override
    public ProductDTO getPromotion() throws IOException {
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
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    private ProductDTO getCarinetProduct(Document document) {
        Elements elements = document.getElementsByClass(hotShotTag);
        ProductDTO productDTO = new ProductDTO(shopName, productURL);

        String imageUrl = elements.select(productImageTag).attr("src");
        productDTO.setPictureUrl(imageUrl);
        productDTO.setProductName(elements.select(productNameTag).text());

        String productUrl = elements.select(productUrlTag).first().text();
        productDTO.setProductUrl(productUrl);

        Element oldPrice = document.select(newPriceTag).first();
        productDTO.setOldPrice(oldPrice.text());

        Element newPrice = document.select(oldPriceTag).first();
        productDTO.setNewPrice(newPrice.text());

        String amount = elements.select(amountTag).select("span").text().replaceAll("[^\\d]", "");
        productDTO.setAmount(amount.replaceAll("[^\\d]", ""));
        return productDTO;
    }


}
