package pl.promotion.finder.feature.promotion.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.product.dto.PriceMapper;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.dto.ProductDTOBuilder;
import pl.promotion.finder.feature.promotion.dto.KomputronikDTO;
import pl.promotion.finder.feature.promotion.dto.KomputronikPricesDTO;
import pl.promotion.finder.feature.promotion.dto.KomputronikProductDTO;
import pl.promotion.finder.utils.DataDownloader;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@Log4j2
@Service
public class KomputronikService implements Promotion {
    private static final String SHOP_NAME = "komputronik";
    private static final String API_URL = "https://www.komputronik.pl/frontend-api/product/box/occasions";
    private static final String API_HEADER = "User-Agent";
    private static final String API_KEY = "Mozilla 5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.0.11) ";
    private KomputronikDTO komputronikDTO;

    public KomputronikDTO getProductDTO() {
        return komputronikDTO;
    }

    public void setProductDTO(KomputronikDTO komputronikDTO) {
        this.komputronikDTO = komputronikDTO;
    }

    public ProductDTO getPromotion() {
        try {
            String data = DataDownloader.fetchData(API_URL, API_HEADER, API_KEY);
            Gson gson = new Gson();
            setProductDTO(gson.fromJson(data, KomputronikDTO.class));
            return getProduct(null);
        } catch (ParseException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        if (getProductDTO().getProducts().isEmpty()) {
            return null;
        }

        Optional<KomputronikProductDTO> optionalProduct = getProductDTO().getProducts().stream().findFirst();
        if (!optionalProduct.isPresent()) {
            return null;
        }

        KomputronikProductDTO product = optionalProduct.get();
        String currency = product.getPrices().getPrice_currency();
        PriceMapper.setCurrency(currency);
        KomputronikPricesDTO prices = product.getPrices();

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(product.getUrl())
                .withProductName(product.getName())
                .withPictureUrl(product.getAlternative_image_url())
                .withOldPrice(prices.getPrice_base_gross())
                .withNewPrice(prices.getFinal_price())
                .build();
    }
}
