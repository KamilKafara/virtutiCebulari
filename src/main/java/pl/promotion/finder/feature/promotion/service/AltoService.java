package pl.promotion.finder.feature.promotion.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.dto.ProductDTOBuilder;
import pl.promotion.finder.feature.promotion.dto.xkom.XkomDTO;
import pl.promotion.finder.utils.DataDownloader;

import java.io.IOException;
import java.text.ParseException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Log4j2
@Service
public class AltoService implements Promotion {
    private static final String API_URL = "https://mobileapi.x-kom.pl/api/v1/alto/hotShots/current";
    private static final String API_HEADER = "x-api-key";
    private static final String API_KEY = "L2BBIXx5zPfPcFU4";
    private static final String SHOP_NAME = "al.to";
    private static final String PRODUCT_URL = "https://www.al.to/";

    @Override
    public ProductDTO getPromotion() {
        try {
            String data = DataDownloader.fetchData(API_URL, API_HEADER, API_KEY);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
            XkomDTO value = objectMapper.readValue(data, XkomDTO.class);
            return getProduct(value);

        } catch (NullPointerException | ParseException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        return null;
    }

    public ProductDTO getProduct(XkomDTO dto) throws ParseException {
        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(PRODUCT_URL)
                .withProductName(dto.getPromotionName())
                .withPictureUrl(dto.getPromotionPhoto().getUrl())
                .withOldPrice(String.valueOf(dto.getOldPrice()))
                .withNewPrice(String.valueOf(dto.getPrice()))
                .build();
    }
}
