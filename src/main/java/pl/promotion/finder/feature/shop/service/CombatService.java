package pl.promotion.finder.feature.shop.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class CombatService {
    public ProductDTO getCombat() throws IOException {

        URL urlCombat = new URL("https://www.combat.pl/");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlCombat.openStream()));
        String inputLine;
        StringBuilder combatSB = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            combatSB.append(inputLine);
//            System.out.println(inputLine);
        }
        Document combatDocument = Jsoup.parse(combatSB.toString());
        return getCombatProduct(combatDocument);
    }

    private ProductDTO getCombatProduct(Document document) {
        Elements title = document.getElementsByClass("div.deals-of");
        Elements box = document.getElementsByClass("price");

        return new ProductDTO();
    }

}
