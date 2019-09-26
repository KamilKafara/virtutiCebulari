package pl.promotion.finder.virtutiCebulari.feature.combat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.promotion.finder.virtutiCebulari.feature.dto.ProductDTO;

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
        }
        Document combatDocument = Jsoup.parse(combatSB.toString());
        return getCombatProduct(combatDocument);
    }

    private ProductDTO getCombatProduct(Document document) {
        Elements pElements = document.select("a");


        return new ProductDTO();
    }

}
