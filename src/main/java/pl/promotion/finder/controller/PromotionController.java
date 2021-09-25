package pl.promotion.finder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.PromotionService;
import pl.promotion.finder.feature.shop.dto.Shop;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
public class PromotionController {
    private final PromotionService promotionService;
    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public List<ProductDTO> getAllPromotion() throws IOException, ParseException {
        return promotionService.getDailyPromotion();
    }

    @GetMapping("/shops")
    public Shop[] getShopEndpoint() {
        return promotionService.getAllEndpoints();
    }
}
