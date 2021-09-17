package pl.promotion.finder.feature.promotion.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.dto.ProductDTOBuilder;
import pl.promotion.finder.feature.promotion.dto.CombatDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;

@Log4j2
@Service
public class CombatService implements Promotion {
    private static final String SHOP_NAME = "combat";
    private static final String SHOP_URL = "https://www.combat.pl/media/catalog/product";
    private static final String JSON_URL = "https://www.combat.pl/rest/V1/get-hot-shot";
    private static final Document EMPTY_DOCUMENT = null;
    private CombatDTO combatDTO;

    public ProductDTO getPromotion() throws IOException {
        URL url = new URL(JSON_URL);
        try {
            prepareJsonFromUrl(url);
            return getProduct(EMPTY_DOCUMENT);
        } catch (ParseException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    private void prepareJsonFromUrl(URL url) throws IOException {
        String ioURL = IOUtils.toString(url, String.valueOf(StandardCharsets.UTF_8));

        Gson gson = new Gson();
        List<String> jsonList = gson.fromJson(ioURL, List.class);

        ModelMapper modelMapper = new ModelMapper();
        if (!jsonList.isEmpty()) {
            this.combatDTO = modelMapper.map(jsonList.stream().findFirst(), (Type) CombatDTO.class);
        }
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        String pictureUrl = SHOP_URL + combatDTO.getPhoto();
        String oldPrice = parsePrice(combatDTO.getRegular_price());
        String newPrice = parsePrice(combatDTO.getPromotion_price());
        String amount = String.valueOf(combatDTO.getLeft());

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(combatDTO.getRegular_url())
                .withProductName(combatDTO.getName())
                .withPictureUrl(pictureUrl)
                .withOldPrice(oldPrice)
                .withNewPrice(newPrice)
                .withAmount(amount)
                .build();
    }

    private String parsePrice(String price) {
        Document doc = Jsoup.parse(price);
        Element priceTag = doc.select("span.price").first();
        return priceTag.text();
    }
}
