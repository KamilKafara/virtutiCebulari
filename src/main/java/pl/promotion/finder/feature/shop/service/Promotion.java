package pl.promotion.finder.feature.shop.service;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;

import java.io.IOException;

@Service
public interface Promotion {
    ProductDTO getPromotion() throws IOException;

    ProductDTO getProduct(Document document);

}
