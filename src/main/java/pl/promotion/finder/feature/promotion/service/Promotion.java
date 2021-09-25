package pl.promotion.finder.feature.promotion.service;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;

import java.io.IOException;
import java.text.ParseException;

@Service
public interface Promotion {
    ProductDTO getPromotion() throws IOException, ParseException;

    ProductDTO getProduct(Document document) throws ParseException;

}
