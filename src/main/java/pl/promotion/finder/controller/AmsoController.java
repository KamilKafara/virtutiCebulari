package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.shop.service.AmsoService;

@RestController
@RequestMapping("/amso")
public class AmsoController {

    private final AmsoService amsoService;

    public AmsoController(AmsoService amsoService) {
        this.amsoService = amsoService;
    }

    @GetMapping
    public ProductDTO getPromotion() {
        return amsoService.getPromotion();
    }
}
