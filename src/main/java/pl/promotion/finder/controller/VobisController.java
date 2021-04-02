package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.VobisService;

import java.io.IOException;

@Deprecated
@RestController
@RequestMapping("/vobis")
public class VobisController {
    private final VobisService vobisService;

    public VobisController(VobisService vobisService) {
        this.vobisService = vobisService;
    }

    @GetMapping
    public ProductDTO getPromotion() throws IOException {
        return vobisService.getPromotion();
    }

}
