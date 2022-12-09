package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.AllWeldService;

@RestController
@RequestMapping("/allWeld")
public class AllWeldController {

    private final AllWeldService allWeldService;

    public AllWeldController(AllWeldService allWeldService) {
        this.allWeldService = allWeldService;
    }

    @GetMapping
    public ProductDTO getPromotion() {
        return allWeldService.getPromotion();
    }
}
