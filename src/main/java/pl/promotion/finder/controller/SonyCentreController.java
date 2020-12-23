package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.SonyCentreService;

import java.io.IOException;

@RestController
@RequestMapping("/sonycentre")
public class SonyCentreController {

    private final SonyCentreService sonyCentreService;

    public SonyCentreController(SonyCentreService sonyCentreService) {
        this.sonyCentreService = sonyCentreService;
    }

    @GetMapping
    public ProductDTO getPromotion() throws IOException {
        return sonyCentreService.getPromotion();
    }
}
