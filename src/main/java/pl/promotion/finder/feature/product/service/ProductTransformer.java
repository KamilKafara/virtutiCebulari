package pl.promotion.finder.feature.product.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.repository.Product;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.Shop;
import pl.promotion.finder.feature.shop.service.ShopTransformer;

@Service
public class ProductTransformer {
    @Autowired
    private ShopTransformer shopTransformer;

    public ProductDTO convertToDto(Product product) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        ShopDTO shopDTO = shopTransformer.convertToDTO(product.getShop());
        productDTO.setShop(shopDTO);
        return productDTO;
    }

    public Product convertFromDto(ProductDTO productDTO) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Product product = modelMapper.map(productDTO, Product.class);
        Shop shop = shopTransformer.convertFromDTO(productDTO.getShop());
        product.setShop(shop);
        return product;

    }
}
