package pl.promotion.finder.virtutiCebulari.feature.komputronik;

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
public class KomputronikService {
    public ProductDTO getKomputronikProduct() throws IOException {
        URL urlKomputronik = new URL("https://www.komputronik.pl/");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlKomputronik.openStream()));
        String inputLine;
        StringBuilder komputronikSB = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            komputronikSB.append(inputLine);
        }
        Document document = Jsoup.parse(komputronikSB.toString());

        Elements elements = document.select("div");
        System.out.println(elements);

        return null;
    }
}
