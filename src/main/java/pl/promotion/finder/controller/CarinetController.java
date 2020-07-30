package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.shop.dto.ProductDTO;
import pl.promotion.finder.feature.shop.service.CarinetService;

import java.io.IOException;

@RestController
@RequestMapping("/carinet")
public class CarinetController {

    private final CarinetService carinetService;

    public CarinetController(CarinetService carinetService) {
        this.carinetService = carinetService;
    }

    @GetMapping
    public ProductDTO getPromotion() throws IOException {
        return carinetService.getPromotion();
    }

}
