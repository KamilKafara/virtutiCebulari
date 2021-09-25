package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.AmsoService;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/amso")
public class AmsoController {

    private final AmsoService amsoService;

    public AmsoController(AmsoService amsoService) {
        this.amsoService = amsoService;
    }

    @GetMapping
    public ProductDTO getPromotion() throws IOException, ParseException {
        return amsoService.getPromotion();
    }
}
