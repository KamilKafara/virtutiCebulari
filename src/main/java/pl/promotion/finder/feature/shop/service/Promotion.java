package pl.promotion.finder.feature.shop.service;

import org.jsoup.nodes.Document;
import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.IOException;

public interface Promotion {
    ProductDTO getPromotion() throws IOException;

    ProductDTO getProduct(Document document);

}
