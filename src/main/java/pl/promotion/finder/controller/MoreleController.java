package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.MoreleService;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/morele")
public class MoreleController {

    private final MoreleService moreleService;

    public MoreleController(MoreleService moreleService) {
        this.moreleService = moreleService;
    }

    @GetMapping
    public ProductDTO getMethod() throws IOException, ParseException {
        return moreleService.getPromotion();
    }
}
