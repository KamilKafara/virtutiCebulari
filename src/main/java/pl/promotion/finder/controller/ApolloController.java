package pl.promotion.finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.promotion.service.ApolloService;

@RestController
@RequestMapping("/apollo")
public class ApolloController {

    private final ApolloService apolloService;

    public ApolloController(ApolloService apolloService) {
        this.apolloService = apolloService;
    }

    @GetMapping
    public ProductDTO getMethod() {
        return apolloService.getPromotion();
    }
}
