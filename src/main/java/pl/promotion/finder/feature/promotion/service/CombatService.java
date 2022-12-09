package pl.promotion.finder.feature.promotion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.dto.ProductDTOBuilder;
import pl.promotion.finder.feature.promotion.dto.combat.CombatDTO;
import pl.promotion.finder.utils.DataDownloader;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;

@Log4j2
@Service
public class CombatService implements Promotion {
    private static final String SHOP_NAME = "combat";
    private static final String SHOP_URL = "https://www.combat.pl/media/catalog/product";
    private static final String API_URL = "https://www.combat.pl/rest/V1/get-hot-shot";
    private static final Document EMPTY_DOCUMENT = null;
    private CombatDTO combatDTO;

    public CombatDTO getCombatDTO() {
        return combatDTO;
    }

    public void setCombatDTO(CombatDTO combatDTO) {
        this.combatDTO = combatDTO;
    }

    public ProductDTO getPromotion() {
        try {
            String data = DataDownloader.fetchData(API_URL);
            ObjectMapper objectMapper = new ObjectMapper();
            CombatDTO[] value = objectMapper.readValue(data, CombatDTO[].class);
            Optional<CombatDTO> dtoOptional = Arrays.stream(value).findFirst();
            dtoOptional.ifPresent(this::setCombatDTO);
            return getProduct(EMPTY_DOCUMENT);
        } catch (ParseException | IOException ex) {
            log.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found promotion in " + SHOP_NAME, new FieldInfo(SHOP_NAME, ErrorCode.NOT_FOUND)));
            log.error(ex.getStackTrace());
        }
        return null;
    }

    @Override
    public ProductDTO getProduct(Document document) throws ParseException {
        CombatDTO dto = getCombatDTO();
        String pictureUrl = dto.getPhoto();
        String oldPrice = parsePrice(dto.getRegularPrice());
        String newPrice = parsePrice(dto.getPromoPrice());
        String amount = String.valueOf(dto.getLeft());

        return new ProductDTOBuilder()
                .withShopName(SHOP_NAME)
                .withProductUrl(dto.getRegularUrl())
                .withProductName(dto.getTitle())
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
