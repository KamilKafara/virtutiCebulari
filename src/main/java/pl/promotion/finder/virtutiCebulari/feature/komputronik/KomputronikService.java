package pl.promotion.finder.virtutiCebulari.feature.komputronik;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;
import pl.promotion.finder.virtutiCebulari.feature.promotion.dto.ProductDTO;

import java.io.IOException;
import java.util.List;

@Service
public class KomputronikService {
    public ProductDTO getKomputronikProduct() throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setPopupBlockerEnabled(true);
        webClient.getOptions().setTimeout(1000);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);

        HtmlPage page = webClient.getPage("https://www.komputronik.pl/");
//        List<DomElement> productList = page.getByXPath("//div@['special-offer2']");
//        for (DomElement division : productList) {
//            System.out.println(division.asText());
//        }

        List<HtmlElement> items = page.getByXPath("//div[@class='special-offer2']");
        if (items.isEmpty()) {
            System.out.println("No items found !");
        } else {
            for (HtmlElement item : items) {
                HtmlAnchor itemAnchor = item.getFirstByXPath(".//p[@class='result-info']/a");

                HtmlElement spanPrice = item.getFirstByXPath(".//a/span[@class='result-price']");

                String itemName = itemAnchor.asText();
                String itemUrl = itemAnchor.getHrefAttribute();

                // It is possible that an item doesn't have any price
                String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();

                System.out.println(String.format("Name : %s Url : %s Price : %s", itemName, itemPrice, itemUrl));
            }
        }
//        System.out.println(page.asText());

        return null;
    }
}
