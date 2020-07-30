package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.shop.dto.ProductDTO;
import pl.promotion.finder.feature.shop.service.XkomService;

import java.io.IOException;

@RestController
@RequestMapping("/xkom")
public class XkomControllerController {

    private final XkomService xkomService;

    public XkomControllerController(XkomService xkomService) {
        this.xkomService = xkomService;
    }

    @GetMapping
    private ProductDTO getPromotionByShop() throws IOException {
        return xkomService.getPromotion();
    }

}
