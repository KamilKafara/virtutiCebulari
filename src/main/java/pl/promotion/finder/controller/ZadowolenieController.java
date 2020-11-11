package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.ZadowolenieService;

@RestController
@RequestMapping("/zadowolenie")
public class ZadowolenieController {

    private final ZadowolenieService zadowolenieService;

    public ZadowolenieController(ZadowolenieService zadowolenieService) {
        this.zadowolenieService = zadowolenieService;
    }

    @GetMapping
    public ProductDTO getPromotion() {
        return zadowolenieService.getPromotion();
    }
}
