package pl.promotion.finder.feature.promotion.service;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.dto.KomputronikDTO;
import pl.promotion.finder.feature.promotion.dto.KomputronikProductDTO;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
public class KomputronikService implements Promotion {
    private static final String SHOP_NAME = "komputronik";
    private static final String JSON_URL = "https://www.komputronik.pl/frontend-api/product/box/occasions";
    private static final String AMOUNT = "empty";
    private KomputronikDTO komputronikDTO;

    public ProductDTO getPromotion() throws IOException {
        String ioURL = fetchJSON();
        Gson gson = new Gson();

        this.komputronikDTO = gson.fromJson(ioURL, KomputronikDTO.class);
        return getProduct(null);
    }

    private static String fetchJSON() throws IOException {
        URL url = new URL(JSON_URL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.0.11) ");
        InputStream inputFile = urlConnection.getInputStream();
        return IOUtils.toString(inputFile, StandardCharsets.UTF_8.name());
    }

    @Override
    public ProductDTO getProduct(Document document) {
        ProductDTO productDTO = new ProductDTO();
        if (this.komputronikDTO.getProducts().isEmpty()) {
            return null;
        }
        KomputronikProductDTO product = this.komputronikDTO.getProducts().get(0);
        String currency = product.getPrices().getPrice_currency();
        productDTO.setProductName(product.getName());
        productDTO.setProductUrl(product.getUrl());
        productDTO.setShopName(SHOP_NAME);
        productDTO.setAmount(AMOUNT);
        productDTO.setOldPrice(product.getPrices().getPrice_base_gross());
        productDTO.setNewPrice(product.getPrices().getFinal_price() + " " + currency);
        productDTO.setPictureUrl(product.getAlternative_image_url());

        return productDTO;
    }
}
