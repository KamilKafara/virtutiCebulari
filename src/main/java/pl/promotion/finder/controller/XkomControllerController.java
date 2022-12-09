package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.XkomService;

@RestController
@RequestMapping("/xkom")
public class XkomControllerController {

    private final XkomService xkomService;

    public XkomControllerController(XkomService xkomService) {
        this.xkomService = xkomService;
    }

    @GetMapping
    public ProductDTO getPromotionByShop() {
        return xkomService.getPromotion();
    }
}
