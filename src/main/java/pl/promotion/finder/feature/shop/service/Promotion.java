package pl.promotion.finder.feature.shop.service;

import pl.promotion.finder.feature.shop.dto.ProductDTO;

import java.io.IOException;
import java.net.MalformedURLException;

public interface Promotion {
    ProductDTO getPromotion() throws IOException;
}
