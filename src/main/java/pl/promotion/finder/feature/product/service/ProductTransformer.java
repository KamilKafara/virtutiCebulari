package pl.promotion.finder.feature.product.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.repository.Product;

@Service
public class ProductTransformer {

    public ProductDTO convertToDto(Product product) {

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(product, ProductDTO.class);
    }

    public Product convertFromDto(ProductDTO productDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productDTO, Product.class);
    }
}
