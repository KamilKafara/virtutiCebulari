package pl.promotion.finder.feature.shop.service;

import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    private final AmsoService amsoService;
    private final CarinetService carinetService;
    private final CombatService combatService;
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private final ZadowolenieService zadowolenieService;
    private final ApolloService apolloService;

    public PromotionService(AmsoService amsoService, CarinetService carinetService, CombatService combatService, MoreleService moreleService, XkomService xkomService, ZadowolenieService zadowolenieService, ApolloService apolloService) {
        this.amsoService = amsoService;
        this.carinetService = carinetService;
        this.combatService = combatService;
        this.moreleService = moreleService;
        this.xkomService = xkomService;
        this.zadowolenieService = zadowolenieService;
        this.apolloService = apolloService;
    }

    public List<ProductDTO> getDailyPromotion() throws IOException {
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(amsoService.getPromotion());
        productDTOList.add(apolloService.getPromotion());
        productDTOList.add(carinetService.getPromotion());
        productDTOList.add(combatService.getPromotion());
        productDTOList.add(moreleService.getPromotion());
        productDTOList.add(xkomService.getPromotion());
        productDTOList.add(zadowolenieService.getPromotion());
        return productDTOList.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
    }

}
