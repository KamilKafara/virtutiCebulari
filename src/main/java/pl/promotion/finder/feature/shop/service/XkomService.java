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
public class XkomService {
    public ProductDTO getXkom() throws IOException {
        try {
            URL urlXkom = new URL("https://www.x-kom.pl/");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlXkom.openStream()));
            String inputLine;
            StringBuilder xkomSB = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                xkomSB.append(inputLine);
            }
            Document xKomDocument = Jsoup.parse(xkomSB.toString());
            return getXkomProduct(xKomDocument);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in xkom.", new FieldInfo("xkom", ErrorCode.NOT_FOUND));
        }
    }

    private ProductDTO getXkomProduct(Document document) {
        Elements pElements = document.select("div");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1);
        for (Element element : pElements) {
            if (element.hasClass("mbxiax-1 fXZaIQ")) {
                productDTO.setProductName(element.getElementsByClass("sc-1bb6kqq-10 kBnBfM m80syu-0 hGKlIY").text());
                Elements urlBlock = element.getElementsByClass("sc-1tblmgq-0");

                Elements productImgElements = urlBlock.tagName("img");
                for (Element el : productImgElements) {
                    Elements imgs = el.getElementsByTag("img");
                    if (imgs.hasAttr("src")) {
                        Elements pictureURL = el.getElementsByAttribute("src");
                        Element pc = pictureURL.get(0);
                        String picURL = pc.attr("src");
                        productDTO.setPictureUrl(picURL);
                    }
                }

                String oldPrice = element.getElementsByClass("sc-1bb6kqq-5 iWkRRi").text();
                String newPrice = element.getElementsByClass("sc-1bb6kqq-4 cLmEvj").text();
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
;
