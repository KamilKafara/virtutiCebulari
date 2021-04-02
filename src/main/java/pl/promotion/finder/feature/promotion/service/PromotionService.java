package pl.promotion.finder.feature.promotion.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.shop.dto.Shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PromotionService {
    private final AmsoService amsoService;
    private final CarinetService carinetService;
    private final CombatService combatService;
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private final ZadowolenieService zadowolenieService;
    private final ApolloService apolloService;
    private final KomputronikService komputronikService;

    public List<ProductDTO> getDailyPromotion() throws IOException {
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(amsoService.getPromotion());
        productDTOList.add(apolloService.getPromotion());
        productDTOList.add(carinetService.getPromotion());
        productDTOList.add(combatService.getPromotion());
        productDTOList.add(moreleService.getPromotion());
        productDTOList.add(xkomService.getPromotion());
        productDTOList.add(zadowolenieService.getPromotion());
        productDTOList.add(komputronikService.getPromotion());
        return productDTOList.parallelStream()
                .filter(ProductDTO::isFilled)
                .collect(Collectors.toList());
    }

    public Shop[] getAllEndpoints() {
        return Shop.values();
    }
}
