package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.KomputronikService;

import java.io.IOException;

@RestController
@RequestMapping("/komputronik")
public class KomputronikController {

    private final KomputronikService komputronikService;

    public KomputronikController(KomputronikService komputronikService) {
        this.komputronikService = komputronikService;
    }

    @GetMapping
    public ProductDTO getPromotion() throws IOException {
        return komputronikService.getPromotion();
    }
}
