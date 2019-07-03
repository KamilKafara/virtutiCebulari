package pl.promotion.finder.virtutiCebulari.feature.promotion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.virtutiCebulari.feature.promotion.dto.ProductDTO.ProductDTO;
import pl.promotion.finder.virtutiCebulari.feature.promotion.service.PromotionService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    private PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    private List<ProductDTO> getAllPromotion() throws IOException {
        return promotionService.getAllPromotion();
    }

    @GetMapping("/shopName")
    private ProductDTO getPromotionByShop(@PathVariable("shopName") String shopName) throws IOException {
        return promotionService.getPromotionByShop(shopName);
    }

}
