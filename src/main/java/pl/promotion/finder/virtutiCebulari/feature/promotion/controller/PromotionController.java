package pl.promotion.finder.virtutiCebulari.feature.promotion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.promotion.finder.virtutiCebulari.feature.promotion.dto.ProductDTO.ProductDTO;
import pl.promotion.finder.virtutiCebulari.feature.promotion.service.PromotionService;

import java.io.IOException;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    private PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/{url}")
    private ProductDTO parseHtml(@PathVariable("url") String shopUrl) throws IOException {
        return promotionService.parseHtml(shopUrl);
    }
}
