package pl.promotion.finder.feature.shop.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
public class CombatService {
    public ProductDTO getCombat() throws IOException {
        try {
            URL urlCombat = new URL("https://www.combat.pl/");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCombat.openStream()));
            String inputLine;
            StringBuilder combatSB = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                combatSB.append(inputLine);
            }
            Document combatDocument = Jsoup.parse(combatSB.toString());
            return getCombatProduct(combatDocument);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in combat.", new FieldInfo("combat", ErrorCode.NOT_FOUND));
        }
    }

    private ProductDTO getCombatProduct(Document document) {
        return new ProductDTO();
    }

}
