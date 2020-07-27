package pl.promotion.finder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.promotion.finder.feature.shop.dto.ProductDTO;
import pl.promotion.finder.feature.shop.service.PromotionService;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/promotion")
public class PromotionController {
    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    private List<ProductDTO> getAllPromotion() throws IOException {
        return promotionService.getAllPromotion();
    }

    @GetMapping("/{shopName}")
    private ProductDTO getPromotionByShop(@PathVariable("shopName") String shopName) throws IOException {
        return promotionService.getPromotionByShop(shopName);
    }

}
