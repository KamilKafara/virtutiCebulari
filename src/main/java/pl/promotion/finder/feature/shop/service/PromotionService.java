package pl.promotion.finder.feature.shop.service;

import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Service
public class PromotionService {
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private final CombatService combatService;
    private final AmsoService amsoService;
    private final CarinetService carinetService;
    private final AltoService altoService;

    public PromotionService(MoreleService moreleService, XkomService xkomService, CombatService combatService, AmsoService amsoService, CarinetService carinetService, AltoService altoService) {
        this.moreleService = moreleService;
        this.xkomService = xkomService;
        this.combatService = combatService;
        this.amsoService = amsoService;
        this.carinetService = carinetService;
        this.altoService = altoService;
    }

    public TreeSet<ProductDTO> getDailyPromotion() throws IOException {
        TreeSet <ProductDTO> productDTOList = new TreeSet <>();
        productDTOList.add(xkomService.getPromotion());
        productDTOList.add(moreleService.getPromotion());
        productDTOList.add(carinetService.getPromotion());
        return productDTOList;
    }

}
