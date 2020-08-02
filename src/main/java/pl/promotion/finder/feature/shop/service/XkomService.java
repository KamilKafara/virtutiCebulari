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
public class XkomService implements Promotion {

    private static final String hotShotTag = "div.sc-bwzfXH.sc-1bb6kqq-2.cNKcdN.sc-htpNat.gSgMmi";
    private static final String propertyTag = "sc-1tblmgq-1 grqydx";
    private static final String oldPriceTag = "sc-1bb6kqq-4";
    private static final String newPriceTag = "sc-1bb6kqq-5";
    private static final String productNameTag = "sc-1bb6kqq-10 kBnBfM m80syu-0 hGKlIY";
    private static final String amountValue = "empty";
    private static final String productImageTag = "src";
    private static final String shopName = "x-kom";
    private static final String shopURL = "https://www.x-kom.pl/";
    private static final String productURL = "https://www.x-kom.pl/goracy_strzal";

    @Override
    public ProductDTO getPromotion() throws IOException {
        try {
            URL urlXkom = new URL(shopURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlXkom.openStream(),"UTF8"));
            String inputLine;
            StringBuilder xkomSB = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                xkomSB.append(inputLine);
            }
            Document xKomDocument = Jsoup.parse(xkomSB.toString());
            return getXkomProduct(xKomDocument);
        } catch (NullPointerException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + shopName, new FieldInfo(shopName, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    private ProductDTO getXkomProduct(Document document) {
        Element element = document.select(hotShotTag).first();
        ProductDTO productDTO = new ProductDTO(shopName, productURL);
        Elements productProperty = element.getElementsByClass(propertyTag);
        productDTO.setPictureUrl(productProperty.attr(productImageTag));
        productDTO.setProductName(element.getElementsByClass(productNameTag).text());
        String oldPrice = element.getElementsByClass(oldPriceTag).text();
        String newPrice = element.getElementsByClass(newPriceTag).text();
        productDTO.setAmount(amountValue);
        productDTO.setOldPrice(oldPrice);
        productDTO.setNewPrice(newPrice);
        return productDTO;
    }
}
