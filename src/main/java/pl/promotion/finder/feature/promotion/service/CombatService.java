package pl.promotion.finder.feature.promotion.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.dto.CombatDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Log4j2
@Service
public class CombatService {
    private static final String AMOUNT_VALUE = "empty";
    private static final String SHOP_NAME = "combat";
    private static final String SHOP_URL = "https://www.combat.pl";
    private static final String JSON_URL = "https://www.combat.pl/rest/V1/get-hot-shot";

    public ProductDTO getPromotion() throws IOException {
        URL url = new URL(JSON_URL);
        String ioURL = IOUtils.toString(url, String.valueOf(StandardCharsets.UTF_8));

        Gson gson = new Gson();
        List<String> combatDTO = gson.fromJson(ioURL, List.class);

        ModelMapper modelMapper = new ModelMapper();
        CombatDTO combatDTO1 = modelMapper.map(combatDTO.get(0), (Type) CombatDTO.class);
        return convertToProductDTO(combatDTO1);
    }

    public ProductDTO convertToProductDTO(CombatDTO combatDTO) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(combatDTO.getName());
        productDTO.setProductUrl(combatDTO.getRegular_url());
        productDTO.setShopName(SHOP_NAME);
        productDTO.setAmount(AMOUNT_VALUE);
        productDTO.setOldPrice(parsePrice(combatDTO.getRegular_price()));
        productDTO.setNewPrice(parsePrice(combatDTO.getPromotion_price()));
        productDTO.setProductUrl(combatDTO.getRegular_url());
        productDTO.setPictureUrl(SHOP_URL + combatDTO.getPhoto());

        return productDTO;
    }

    private String parsePrice(String price) {
        Document doc = Jsoup.parse(price);
        Element priceTag = doc.select("span.price").first();
        return priceTag.text();
    }
}
