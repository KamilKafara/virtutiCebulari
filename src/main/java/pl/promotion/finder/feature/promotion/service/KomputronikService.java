package pl.promotion.finder.feature.promotion.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.product.dto.PriceMapper;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.dto.KomputronikDTO;
import pl.promotion.finder.feature.promotion.dto.KomputronikProductDTO;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

@Log4j2
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
        try {
            return getProduct(null);
        } catch (ParseException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    private static String fetchJSON() throws IOException {
        URL url = new URL(JSON_URL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.0.11) ");
        InputStream inputFile = urlConnection.getInputStream();
        return IOUtils.toString(inputFile, StandardCharsets.UTF_8.name());
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        ProductDTO productDTO = new ProductDTO();
        if (this.komputronikDTO.getProducts().isEmpty()) {
            return null;
        }
        KomputronikProductDTO product = this.komputronikDTO.getProducts().get(0);
        productDTO.setProductName(product.getName());
        productDTO.setProductUrl(product.getUrl());
        productDTO.setShopName(SHOP_NAME);
        productDTO.setAmount(AMOUNT);
        String currency = product.getPrices().getPrice_currency();
        PriceMapper.setCurrency(currency);
        productDTO.setOldPrice(PriceMapper.priceFactory(product.getPrices().getPrice_base_gross()));
        productDTO.setNewPrice(PriceMapper.priceFactory(product.getPrices().getFinal_price()));
        productDTO.setPictureUrl(product.getAlternative_image_url());
        return productDTO;
    }
}
