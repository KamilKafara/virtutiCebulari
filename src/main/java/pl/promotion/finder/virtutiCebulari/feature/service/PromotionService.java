package pl.promotion.finder.virtutiCebulari.feature.service;

import org.springframework.stereotype.Service;
import pl.promotion.finder.virtutiCebulari.feature.amso.AmsoService;
import pl.promotion.finder.virtutiCebulari.feature.combat.CombatService;
import pl.promotion.finder.virtutiCebulari.feature.dto.ProductDTO;
import pl.promotion.finder.virtutiCebulari.feature.komputronik.KomputronikService;
import pl.promotion.finder.virtutiCebulari.feature.morele.MoreleService;
import pl.promotion.finder.virtutiCebulari.feature.xkom.XkomService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService {
    private final MoreleService moreleService;
    private final XkomService xkomService;
    private final KomputronikService komputronikService;
    private final CombatService combatService;
    private final AmsoService amsoService;

    public PromotionService(MoreleService moreleService, XkomService xkomService, KomputronikService komputronikService, CombatService combatService, AmsoService amsoService) {
        this.moreleService = moreleService;
        this.xkomService = xkomService;
        this.komputronikService = komputronikService;
        this.combatService = combatService;
        this.amsoService = amsoService;
    }

    public List<ProductDTO> getAllPromotion() throws IOException {
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(xkomService.getXkom());
        productDTOList.add(moreleService.getMorele());
        productDTOList.add(amsoService.getAmso());
        return productDTOList;
    }

    public ProductDTO getPromotionByShop(String shopName) throws IOException {
        switch (shopName) {
            case "xkom": {
                return xkomService.getXkom();
            }
            case "morele": {
                return moreleService.getMorele();
            }
            case "komputronik": {
                return komputronikService.getKomputronikProduct();
            }
            case "combat": {
                return combatService.getCombat();
            }
            case "amso": {
                return amsoService.getAmso();
            }
            default: {
                return null;
            }
        }
    }
}
