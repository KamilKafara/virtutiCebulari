package pl.promotion.finder.feature.promotion.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.shop.dto.Shop;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PromotionService {
    private final AmsoService amsoService;
    private final CombatService combatService;
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private final KomputronikService komputronikService;

    public List<ProductDTO> getDailyPromotion() {
        Set<ProductDTO> productDTOList = new HashSet<>();

        productDTOList.add(amsoService.getPromotion());
        productDTOList.add(combatService.getPromotion());
        productDTOList.add(moreleService.getPromotion());
        productDTOList.add(xkomService.getPromotion());
        productDTOList.add(komputronikService.getPromotion());
        return productDTOList.parallelStream()
                .filter(ProductDTO::isFilled)
                .collect(Collectors.toList());
    }

    public Shop[] getAllEndpoints() {
        return Shop.values();
    }
}
