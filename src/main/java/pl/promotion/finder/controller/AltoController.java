package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.shop.dto.ProductDTO;
import pl.promotion.finder.feature.shop.service.AltoService;

import java.io.IOException;

@RestController

@RequestMapping("/alto")
public class AltoController {

    private final AltoService altoService;

    public AltoController(AltoService altoService) {
        this.altoService = altoService;
    }

    @GetMapping
    public ProductDTO getPromotion() throws IOException {
        return altoService.getPromotion();
    }
}